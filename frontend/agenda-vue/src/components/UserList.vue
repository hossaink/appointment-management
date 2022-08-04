<template>
  <div class="q-my-md row">
    <q-card-section style="width: 100%">
      <q-expansion-item
        dense
        dense-toggle
        expand-separator
        class="col-12 shadow-1 overflow-hidden"
        icon="add_circle"
        label="Add new user"
      >
        <q-card>
          <q-card-section>
            <q-input
              dense
              outlined
              unmasked-value
              v-model="newUser"
              label="Name"
            >
              <template v-slot:before>
                <label class="text-subtitle2">
                  Enter name of user
                </label></template
              ></q-input
            >
          </q-card-section>

          <q-separator />
          <q-card-actions align="right" class="q-col-gutter-x-xl">
            <q-btn
              color="green"
              icon="done"
              label="Add"
              @click="addNewUser()"
            />
            <q-btn
              color="red"
              icon="clear"
              label="Cancel"
              @click="newUser = ''"
            />
          </q-card-actions>
        </q-card>
      </q-expansion-item>

      <div class="q-my-md">
        <q-scroll-area
          :thumb-style="thumbStyle"
          :bar-style="barStyle"
          id="scroll-area-with-virtual-scroll-1"
          :style="'height: ' + 37 * 100 + 'px'"
        >
          <q-virtual-scroll
            scroll-target="#scroll-area-with-virtual-scroll-1 > .scroll"
            :items="users"
            :virtual-scroll-item-size="32"
            separator
            bordered
          >
            <template v-slot="{ item, index }">
              <q-item :key="index" dense>
                <q-item-section>
                  <q-item
                    dense
                    dense-toggle
                    expand-separator
                    :class="item.deleted ? 'bg-grey-3 text-grey-8' : ''"
                    class="col-11"
                    :disable="item.deleted"
                    icon="perm_identity"
                    :label="item.name"
                  >
                    {{ item.name }}
                  </q-item>
                </q-item-section>
                <q-item-section top side>
                  <div
                    :class="
                      item.deleted
                        ? 'text-green q-gutter-xs'
                        : 'text-red q-gutter-xs'
                    "
                  ></div>
                </q-item-section>
              </q-item>
            </template>
          </q-virtual-scroll>
        </q-scroll-area>
      </div>
    </q-card-section>
  </div>
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
    var newUser = ref(null);

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

    function addNewUser() {
      if (newUser.value) {
        api
          .post(
            "/user/add",
            {},
            {
              params: {
                name: newUser.value,
              },
            }
          )
          .then(() => {
            users.value.push({ name: newUser.value });
            newUser = ref(null);
            $q.notify({
              color: "positive",
              position: "top",
              message: "New user created successfully",
              icon: "tick",
            });
          })
          .catch(() => {
            $q.notify({
              color: "negative",
              position: "top",
              message: "Booking failed",
              icon: "report_problem",
            });
          });
      } else {
        $q.notify({
          color: "negative",
          position: "top",
          message: "I need name to create a user!",
          icon: "report_problem",
        });
      }
    }
    return {
      currentUser: ref(null),
      newUser,
      users,
      loadUsers,
      addNewUser,
      thumbStyle: {
        right: "5px",
        borderRadius: "8px",
        backgroundColor: "#027be3",
        width: "8px",
        opacity: 0.75,
      },

      barStyle: {
        right: "2px",
        borderRadius: "14px",
        backgroundColor: "#027be3",
        width: "14px",
        opacity: 0.2,
        marginTop: "-3px",
        marginBottom: "-3px",
        paddingTop: "3px",
        paddingBottom: "3px",
      },
    };
  },
  mounted() {
    this.loadUsers();
  },
};
</script>
