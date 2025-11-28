import requests
import csv
import random
import string
import logging
import json
import time
import os
from faker import Faker

# --- CONFIGURAÇÕES ---
BASE_URL = "http://localhost:8080/api"
CSV_FILENAME = "users_credentials.csv"
LOCALE = "pt_BR" 
TEMP_IMG_NAME = "temp_pet_image.jpg" # Nome temporário para o arquivo baixado

# Quantidade de dados
NUM_USERS = 0
NUM_ORGS = 0 
NUM_PETS = 10000 

# Configuração de Log
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("population_errors.log"),
        logging.StreamHandler()
    ]
)

fake = Faker(LOCALE)

# --- FUNÇÕES AUXILIARES GERAIS ---

def generate_password(length=12):
    chars = string.ascii_letters + string.digits + "!@#$%&"
    return ''.join(random.choice(chars) for _ in range(length))

def generate_cpf():
    return fake.cpf().replace('.', '').replace('-', '')

def get_json_headers(token=None):
    headers = {'Content-Type': 'application/json'}
    if token:
        headers['Authorization'] = f"Bearer {token}"
    return headers

# --- FUNÇÕES DE DOWNLOAD DE IMAGEM ---

def download_dog_image():
    """Obtém URL do DogAPI, baixa os bytes e salva localmente."""
    try:
        # 1. Pega a URL
        resp = requests.get("https://dog.ceo/api/breeds/image/random", timeout=5)
        if resp.status_code == 200:
            data = resp.json()
            if data.get("status") == "success":
                image_url = data.get("message")
                
                # 2. Baixa o binário da imagem
                img_resp = requests.get(image_url, timeout=10)
                if img_resp.status_code == 200:
                    with open(TEMP_IMG_NAME, 'wb') as f:
                        f.write(img_resp.content)
                    return True
    except Exception as e:
        logging.warning(f"Erro ao baixar imagem de cachorro: {e}")
    return False

def download_cat_image():
    """Baixa os bytes diretamente do Cataas e salva localmente."""
    try:
        # Cataas retorna o binário direto nesta URL
        resp = requests.get("https://cataas.com/cat", timeout=10)
        if resp.status_code == 200:
            with open(TEMP_IMG_NAME, 'wb') as f:
                f.write(resp.content)
            return True
    except Exception as e:
        logging.warning(f"Erro ao baixar imagem de gato: {e}")
    return False

# --- FLUXO PRINCIPAL ---

def create_admin_if_needed():
    email = "admin.script@test.com"
    password = "admin_password_123"
    
    payload = {
        "email": email,
        "password": password,
        "name": "Admin",
        "surname": "Script",
        "cpf": generate_cpf(),
        "phone": "11999999999",
        "roles": ["ADMIN"],
        "cep": "01001000",
        "street": "Rua Admin",
        "number": "1",
        "neighborhood": "Centro",
        "city": "São Paulo",
        "uf": "SP"
    }
    
    try:
        requests.post(f"{BASE_URL}/auth/register", json=payload, headers=get_json_headers())
    except Exception:
        pass # Ignora se já existir

    try:
        resp = requests.post(f"{BASE_URL}/auth/login", json={"email": email, "password": password}, headers=get_json_headers())
        if resp.status_code == 200:
            return resp.json()['accessToken']
        else:
            logging.error(f"Falha ao logar como ADMIN: {resp.text}")
            return None
    except Exception as e:
        logging.error(f"Erro ao conectar para login ADMIN: {e}")
        return None

def main():
    try:
        csv_file = open(CSV_FILENAME, mode='w', newline='', encoding='utf-8')
        csv_writer = csv.writer(csv_file)
        csv_writer.writerow(['Name', 'Email', 'Password', 'Role', 'ID'])
        logging.info(f"Arquivo CSV iniciado: {CSV_FILENAME}")
    except Exception as e:
        logging.critical(f"Não foi possível criar o arquivo CSV: {e}")
        return

    admin_token = create_admin_if_needed()
    if not admin_token:
        logging.critical("Abortando: Não foi possível obter token de ADMIN.")
        return

    created_user_ids = []

    # 1. CRIAR USUÁRIOS
    logging.info(f"--- Iniciando criação de {NUM_USERS} Usuários ---")
    for _ in range(NUM_USERS):
        try:
            password = generate_password()
            email = fake.unique.email()
            name = fake.first_name()
            surname = fake.last_name()
            
            payload = {
                "email": email,
                "password": password,
                "name": name,
                "surname": surname,
                "cpf": generate_cpf(),
                "phone": fake.phone_number(),
                "roles": ["USER"],
                "cep": fake.postcode().replace('-', ''),
                "street": fake.street_name(),
                "number": str(fake.building_number()),
                "neighborhood": fake.bairro(),
                "city": fake.city(),
                "uf": fake.state_abbr()
            }

            resp = requests.post(f"{BASE_URL}/auth/register", json=payload, headers=get_json_headers())

            if resp.status_code == 201:
                token_new_user = resp.json().get('accessToken')
                if token_new_user:
                    token = token_new_user.replace("Bearer ", "")
                    # GET /me para pegar ID
                    headers_auth = {'Authorization': f"Bearer {token}"}
                    me_resp = requests.get(f"{BASE_URL}/auth/me", headers=headers_auth)
                    if me_resp.status_code == 200:
                        user_id = me_resp.json()['id']
                        created_user_ids.append(user_id)
                        csv_writer.writerow([f"{name} {surname}", email, password, "USER", user_id])
                        logging.info(f"Usuário criado: {email}")
            else:
                logging.error(f"Erro ao criar usuário {email}: {resp.status_code}")
        except Exception as e:
            logging.error(f"Exceção ao criar usuário: {e}")

    # 2. CRIAR ORGS (ATUALIZADO PARA NOVA CONTROLLER)
    logging.info(f"--- Iniciando criação de {NUM_ORGS} Organizações ---")
    for _ in range(NUM_ORGS):
        try:
            org_name = f"ONG {fake.company()}"
            
            # ATUALIZADO: Agora inclui campos de endereço
            payload = {
                "name": org_name,
                "email": fake.unique.company_email(),
                "phone": fake.phone_number(),
                "petOwnedId": None,
                # Novos campos de endereço:
                "cep": fake.postcode().replace('-', ''),
                "street": fake.street_name(),
                "streetNumber": str(fake.building_number()), # Nota: DTO usa 'streetNumber'
                "neighborhood": fake.bairro(),
                "city": fake.city(),
                "uf": fake.state_abbr()
            }
            
            # Headers com Content-Type JSON explícito
            resp = requests.post(f"{BASE_URL}/orgs", json=payload, headers=get_json_headers())

            if resp.status_code == 201:
                org_data = resp.json()
                if 'id' in org_data:
                    # ONGs não entram na lista de resgatadores por enquanto (devido à restrição de FK no banco)
                    csv_writer.writerow([org_name, payload['email'], "N/A (ORG)", "ORG", org_data['id']])
                    logging.info(f"ONG criada: {org_name}")
            else:
                logging.error(f"Erro ao criar ONG: {resp.status_code} - {resp.text}")
        except Exception as e:
            logging.error(f"Exceção ao criar ONG: {e}")
    
    # 3. CRIAR PETS (COM UPLOAD DE ARQUIVO)
    logging.info(f"--- Iniciando criação de {NUM_PETS} Pets (com download/upload de imagens) ---")
    
    all_rescuers = created_user_ids
    
    pet_species = ['DOG', 'CAT']
    pet_sizes = ['SMALL', 'MEDIUM', 'LARGE']
    pet_status = ['AVAILABLE', 'ADOPTED']

    for i in range(NUM_PETS):
        try:
            time.sleep(0.5) # Pausa para não bloquear IP nas APIs externas
            logging.info(f"Processando pet {i+1}/{NUM_PETS}...")

            rescued_by = random.choice(all_rescuers) if all_rescuers else None
            name = fake.first_name()
            species = random.choice(pet_species)

            # --- BAIXAR IMAGEM ---
            has_image = False
            if species == 'DOG':
                has_image = download_dog_image()
            else:
                has_image = download_cat_image()
            
            # --- DADOS DO PET ---
            # Probabilidades
            has_chronic_disease = random.choice(['DIABETES', 'ALERGIA', 'ARTRITE']) if random.random() < 0.15 else None
            has_special_needs = random.choice(['CADEIRANTE', 'CEGO', 'TRIPÉ']) if random.random() < 0.05 else None
            has_ongoing_treatment = random.random() < 0.9 if has_chronic_disease else random.random() < 0.1
            
            description = f"{name} é um {species} {random.choice(['amigável', 'tímido', 'agitado'])}. {fake.text(max_nb_chars=50)}"

            pet_data = {
                "name": name,
                "species": species,
                "breed": "SRD",
                "size": random.choice(pet_sizes),
                "sex": random.choice(['M', 'F']),
                "status": random.choice(pet_status),
                "rescuedById": rescued_by,
                "goodWithOtherAnimals": random.choice([True, False]),
                "requiresConstantCare": False,
                "petDescription": description,
                "hasSpecialNeeds": has_special_needs,
                "hasOngoingTreatment": has_ongoing_treatment,
                "hasChronicDisease": has_chronic_disease,
                # OBS: Não mandamos "petImage" string aqui, mandamos o arquivo binário abaixo
            }

            # --- PREPARAR MULTIPART ---
            multipart_files = {
                'data': (None, json.dumps(pet_data), 'application/json'), # Parte do JSON
            }

            # Se conseguimos baixar a imagem, anexamos no multipart
            file_handle = None
            if has_image:
                file_handle = open(TEMP_IMG_NAME, 'rb')
                # 'file' é o nome do parâmetro esperado na Controller Spring
                # (TEMP_IMG_NAME, file_handle, 'image/jpeg')
                multipart_files['file'] = (TEMP_IMG_NAME, file_handle, 'image/jpeg')

            # Headers APENAS com Auth (requests gera o Content-Type multipart/boundary automaticamente)
            headers_post = {'Authorization': f"Bearer {admin_token}"}

            resp = requests.post(
                f"{BASE_URL}/pets", 
                headers=headers_post,
                files=multipart_files
            )

            # --- LIMPEZA ---
            if file_handle:
                file_handle.close() # Fecha arquivo para poder deletar
            
            if has_image and os.path.exists(TEMP_IMG_NAME):
                os.remove(TEMP_IMG_NAME) # Deleta o arquivo temporário

            if resp.status_code == 201:
                logging.info(f"Pet criado com sucesso: {name} (Imagem enviada: {has_image})")
            else:
                logging.error(f"Erro ao criar Pet: {resp.status_code} - {resp.text}")

        except Exception as e:
            logging.error(f"Exceção ao criar Pet: {e}")
            # Tenta limpar arquivo se deu erro no meio
            if os.path.exists(TEMP_IMG_NAME):
                try: os.remove(TEMP_IMG_NAME)
                except: pass

    csv_file.close()
    logging.info("--- Execução finalizada. ---")

if __name__ == "__main__":
    main()
