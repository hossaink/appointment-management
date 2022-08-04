<template>
  <q-layout view="hHh lpR fFf">
    <q-header reveal elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />
        <q-toolbar-title>
          {{ currentRouteName }}
        </q-toolbar-title>
        <q-space />
        Welcome {{ userName }}
      </q-toolbar>
    </q-header>
    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered>
      <q-list bordered padding class="rounded-borders text-primary">
        <q-item
          clickable
          v-ripple
          :active="link === 'booking'"
          @click="
            link = 'booking';
            this.$router.push('/');
          "
          active-class="gk-menu-link"
        >
          <q-item-section avatar>
            <q-icon name="beenhere" />
          </q-item-section>

          <q-item-section>Booking</q-item-section>
        </q-item>

        <q-item
          clickable
          v-ripple
          :active="link === 'agenda'"
          @click="
            link = 'agenda';
            this.$router.push('/agenda');
          "
          active-class="gk-menu-link"
        >
          <q-item-section avatar>
            <q-icon name="calendar_month" />
          </q-item-section>

          <q-item-section>Agenda</q-item-section>
        </q-item>

        <q-item
          clickable
          v-ripple
          :active="link === 'users'"
          @click="
            link = 'users';
            this.$router.push('/user');
          "
          active-class="gk-menu-link"
        >
          <q-item-section avatar>
            <q-icon name="group_add" />
          </q-item-section>

          <q-item-section>User Management</q-item-section>
        </q-item>

        <q-separator spaced />

        <q-item
          clickable
          v-ripple
          :active="link === 'about'"
          @click="
            link = 'about';
            this.$router.push('/about');
          "
          active-class="gk-menu-link"
        >
          <q-item-section avatar>
            <q-icon name="help" />
          </q-item-section>

          <q-item-section>About</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>
    <q-page-container class="q-ma-md q-pa-md">
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import { useStore } from "vuex";
import { mapState } from "vuex";
import { ref } from "vue";
export default {
  name: "LayoutDefault",
  computed: {
    userName() {
      return this.$store.state.currentUserName;
    },
    currentRouteName() {
      return this.$route.name;
    },
  },
  setup() {
    const leftDrawerOpen = ref(false);
    const store = useStore();
    const link = ref("booking");
    return {
      currentUserName2: "Welcome " + store.state.currentUserName,
      computed: mapState(["currentUserName"]),
      toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value;
      },
      leftDrawerOpen,
      link,
    };
  },
};
</script>
<style lang="sass">
.gk-menu-link
  color: white
  background: lightblue
</style>
