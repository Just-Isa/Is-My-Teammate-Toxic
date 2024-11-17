import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/links",
    name: "links",
    component: () =>
      import("../views/Links.vue"),
  },
  {
    path: "/mastery",
    name: "mastery",
    component: () =>
      import("../views/ChampionMastery.vue"),
  },
  {
    path: "/sources",
    name: "sources",
    component: () =>
      import("../views/SourcesView.vue"),
  },
  {
    path: "/lol",
    name: "lol",
    component: () =>
      import("../views/LolView.vue"),
  },
  {
    path: '/:catchAll(.*)*',
    name: "error",
    component: () =>
      import('../views/ErrorView.vue'),
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
