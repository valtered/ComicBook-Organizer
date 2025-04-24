import {createRouter, createWebHistory }from 'vue-router';
import {useStore }from 'vuex';
// Import components
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import GuestPage from '../views/GuestPage.vue';
import AddComic from '../views/AddComic.vue';
import CreateCollectionView from '../views/CreateCollectionView.vue';
import ViewCollection from '@/views/ViewCollection.vue';
import CollectionList from '../views/CollectionList.vue';
import Search from '../views/ComicSearch.vue';

const routes = [

  {
    path: '/search',
    name: 'search',
    component: Search,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/guest-home',
    name: 'guest-home',
    component: GuestPage,
    meta: {
      requiresAuth: false
    }

  },
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/logout',
    name: 'logout',
    component: LogoutView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/collection',
    name: 'collection-list',
    component: CollectionList,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/collection/:id',
    name: 'view-collection',
    component: ViewCollection,
    meta: {
      requiresAuth: true // This route requires authentication
    }
  },
  {
    path: '/add-comic',
    name: 'add-comic',
    component: AddComic,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/comic-search',
    component: Search
  },
  {
    path: '/create-collection-view',
    name: 'create-collection-view',
    component: CreateCollectionView,
    meta: {
      requiresAuth: true
    }
  }
];

// Create the router


const router = createRouter({
 history: createWebHistory(),
 routes: routes
});
// Global navigation guard
router.beforeEach((to) => {

 const store = useStore();
 const requiresAuth = to.matched.some((x) => x.meta.requiresAuth);
 if (requiresAuth && store.state.token === '') {
  return {name: 'guest-home' };
 }

});
export default router;
