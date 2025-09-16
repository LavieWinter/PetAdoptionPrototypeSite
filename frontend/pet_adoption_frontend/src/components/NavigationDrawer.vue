<template>

<v-navigation-drawer v-model="drawer" location="left" width="355" height="100%" color="primary"
      :permanent="$vuetify.display.mdAndUp">
      <v-list class="py-0" slim>
        <v-list-item link prepend-icon="mdi-home-outline" title="Dashboard" />

        <v-list-group prepend-icon="mdi-account-multiple-outline" title="Customers">
          <template #activator="{ props: activatorProps }">
            <v-list-item v-bind="activatorProps" />
          </template>

          <v-list-item link prepend-icon="mdi-account-plus-outline" title="Create New" />

          <v-list-group prepend-icon="mdi-magnify" title="Search">
            <template #activator="{ props: activatorProps }">
              <v-list-item v-bind="activatorProps" />
            </template>

            <v-list-item link prepend-icon="mdi-account-outline" title="By Name" />

            <v-list-item link prepend-icon="mdi-email-outline" title="By Email" />

            <v-list-item link prepend-icon="mdi-phone-outline" title="By Phone" />
          </v-list-group>
        </v-list-group>

        <v-list-item link prepend-icon="mdi-calendar" title="Calendar" />

        <v-list-item link prepend-icon="mdi-poll" title="Analytics" />

        <v-divider />

        <v-list-item link prepend-icon="mdi-inbox-outline" title="Inbox" />

        <v-list-item link prepend-icon="mdi-bell-outline" title="Notifications" />

        <v-divider />

        <v-list-item lines="two" link
          prepend-avatar="https://vuetifyjs.b-cdn.net/docs/images/avatars/planetary-subscriber.png"
          subtitle="Vuetify Engineer" title="John Leider" />
      </v-list>
    </v-navigation-drawer>
    </template>

<script setup>
import { ref } from 'vue'
const drawer = ref(true)
</script>