import { createApp, Plugin } from "vue";
import App from "./App.vue";
import router from "./router";
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import 'vue-material-design-icons/styles.css';


const vuetify = createVuetify({
    components,
    directives,
    theme: {
      defaultTheme: "dark",
    }
  });
  
createApp(App).use(router).use(vuetify).mount("#app");
