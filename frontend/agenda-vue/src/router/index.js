import { createRouter, createWebHistory } from "vue-router";
import BookAppointment from "../views/BookAppointment.vue";

const routes = [
  {
    path: "/",
    name: "Book Appointment",
    component: BookAppointment,
  },
  {
    path: "/user",
    name: "User Management",
    component: () =>
      import(
        /* webpackChunkName: "UserManagement" */ "../views/UserManagement.vue"
      ),
  },
  {
    path: "/agenda",
    name: "Agenda",
    component: () =>
      import(/* webpackChunkName: "Agenda" */ "../views/Agenda.vue"),
  },
  {
    path: "/about",
    name: "About",

    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
