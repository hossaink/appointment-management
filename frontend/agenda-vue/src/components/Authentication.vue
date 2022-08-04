<template>
  <q-card flat bordered class="row text-center">
    <q-select
      filled
      v-model="currentUser"
      :options="users"
      option-value="id"
      option-label="name"
      label="Who are you?"
      style="min-width: 85%"
    />
    <q-btn
      color="white"
      icon="key"
      text-color="black"
      label="Login"
      @click="setCurrentUser()"
      style="min-width: 15%"
    />
  </q-card>
</template>
<script>
import { api } from "@/boot/axios";
import { ref } from "vue";
import { useQuasar } from "quasar";

import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const $q = useQuasar();
    const users = ref(null);

    function loadUsers() {
      api
        .get("/user/all")
        .then((response) => {
          users.value = response.data;
          store.commit("updateAllUsers", users.value);
        })
        .catch(() => {
          $q.notify({
            color: "negative",
            position: "top",
            message: "Loading users failed",
            icon: "report_problem",
          });
        });
    }

    function setCurrentUser() {
      if (this.currentUser) {
        store.commit("login", this.currentUser);
        $q.notify({
          color: "positive",
          position: "top",
          message:
            "You logged in securely (wink wink) as " + this.currentUser.name,
          icon: "key",
        });
      } else {
        $q.notify({
          color: "negative",
          position: "top",
          message: "No user selected!",
          icon: "report_problem",
        });
      }
    }
    return { currentUser: ref(null), users, loadUsers, setCurrentUser };
  },
  mounted() {
    this.loadUsers();
  },
};
</script>
