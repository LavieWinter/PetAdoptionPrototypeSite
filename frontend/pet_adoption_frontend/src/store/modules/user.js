// src/store/modules/user.js
import { getMe, updateMe } from '@/services/user'
import { login as apiLogin, register as apiRegister, logout as apiLogout } from '@/services/auth'
import { TOKEN_KEY } from '@/plugins/axios'

export default {
    namespaced: true,

    state: () => ({
        token: localStorage.getItem(TOKEN_KEY) || '',
        profile: null,
        loading: false
    }),

    getters: {
        isAuthed: (s) => !!s.token,
        fullName: (s) =>
            s.profile ? `${s.profile.firstName || ''} ${s.profile.lastName || ''}`.trim() : ''
    },

    mutations: {
        SET_LOADING(state, v) {
            state.loading = v
        },
        SET_TOKEN(state, t) {
            state.token = t || ''
            t ? localStorage.setItem(TOKEN_KEY, t) : localStorage.removeItem(TOKEN_KEY)
        },
        SET_PROFILE(state, p) {
            state.profile = p
        }
    },

    actions: {
        async init({ state, dispatch }) {
            if (state.token) {
                try {
                    await dispatch('fetchProfile')
                } catch {
                    /* token inválido? */
                }
            }
        },

        async login({ commit, dispatch }, { email, password }) {
            commit('SET_LOADING', true)
            try {
                const data = await apiLogin(email, password)
                // token já foi salvo pelo service; sincroniza local
                commit('SET_TOKEN', localStorage.getItem(TOKEN_KEY) || '')
                console.log('Token after login:', localStorage.getItem(TOKEN_KEY));
                await dispatch('fetchProfile')
                return data
            } finally {
                commit('SET_LOADING', false)
            }
        },

        async register({ commit, dispatch }, payload) {
            commit('SET_LOADING', true)
            try {
                const data = await apiRegister(payload)
                commit('SET_TOKEN', localStorage.getItem(TOKEN_KEY) || '')
                await dispatch('fetchProfile')
                return data
            } finally {
                commit('SET_LOADING', false)
            }
        },

        async fetchProfile({ commit, dispatch }) {
            try {
                const me = await getMe()
                commit('SET_PROFILE', me)
                console.log('Fetched profile:', me);
                return me
            } catch (err) {
                // 🔑 se der 401 aqui (token inválido/expirado), limpe o estado
                if (err?.response?.status === 401) {
                    dispatch('logout')
                }
                throw err
            }
        },

        async updateProfile({ commit }, partial) {
            commit('SET_LOADING', true)
            try {
                const updated = await updateMe(partial)
                commit('SET_PROFILE', updated)
                return updated
            } finally {
                commit('SET_LOADING', false)
            }
        },

        logout({ commit }) {
            apiLogout()
            commit('SET_TOKEN', '')
            commit('SET_PROFILE', null)
        }
    }
}
