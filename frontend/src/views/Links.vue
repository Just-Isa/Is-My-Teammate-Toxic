<template>
  <home-button/>
  <Navigation class="navbar"></Navigation>
  <div class="container">
      <div v-for="key in Object.keys(links)" :key="key" class="type-container">
          <v-card
          class="card"
          max-width="500"
          min-width="250"
          >
              <v-card-actions>
              <v-btn
                  color="purple"
                  variant="elevated"
                  @click="goToLink(links[key])"
                  size="x-large"
                  class="card-button mb-1 mt-1"
              >
              {{ key }}
              </v-btn>

              </v-card-actions>
          </v-card>
      </div>
  </div>
</template>

<script setup lang="ts">
  import { onMounted } from 'vue';
  import Navigation from "@/components/Navigation.vue";
  import { useRoute } from 'vue-router';
  import { reactive } from 'vue';

  const route = useRoute();
  const links: { [key: string]: string } = reactive({});


  var xd = ["hi", "hi2", "hi3"];
  onMounted(() => {

      const region = (route.query.region as string).toLowerCase();
      const regionNoNum = (route.query.region as string).toLowerCase().replace(/[0-9]/g, '');
      const tag = route.query.tag;
      const name = route.query.name;

      links["op.gg"] = `https://op.gg/summoners/${regionNoNum}/${name}-${tag}/`;
      links["League of Graphs"] = `https://www.leagueofgraphs.com/summoner/${regionNoNum}/${name}-${tag}/`;
      links["u.gg"] = `https://u.gg/lol/profile/${region}/${name}-${tag}/overview`

      console.log( links)
      console.log(region, tag, name);
  });

  function goToLink(link: string) {
      location.href = link;
  }
</script>

<style>
.container {
  margin-left: 100px;
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: space-around;
  align-items: center;
}

.card{
  flex:1;
}

.card-title {
  margin-bottom: 20px;
}

.card-button{
  margin:0 auto;
  display:block;
}


@media only screen and (max-width: 600px) {
    .card {
      max-width: 250px !important;
    }
  }
</style>
