<template>
    <div class="chart-container">
      <div id="chart-div">
        <GChart
          :type="props.type"
          :data="chartData"
          :options="chartOptions"
        />
      </div>
    </div>
  </template>

<script setup lang="ts">
    import { usePlayerMasteryService } from '@/services/PlayerMasteryService';
    import { useLolChampsService } from '@/services/LolChampService';
    import { GChart } from 'vue-google-charts'
    import { GoogleChartOptions } from 'vue-google-charts/dist/types';
    import { useTheme } from 'vuetify/lib/framework.mjs';

    const lolChampsService = useLolChampsService();
    const playerMasteryService = usePlayerMasteryService();
    const themeService = useTheme();

    // TODO: Make the charts be created according to type
    const props = defineProps<{
        type: 'ColumnChart' | 'PieChart' | 'BarChart';
    }>();

    const chartOptions: GoogleChartOptions = {
        width: 550,
        height: 500,
        colors:  [
            "#683cb4",
            "#9369D4",
            "#3E1E72",
            "#B69CEE",
            "#FFD700",
            "#FF7F50",
            "#00CED1",
            "#32CD32",
            "#FFC0CB",
            "#708090"
        ],
        chartArea: {
            width: props.type === 'PieChart' ? '90%' : '60%'
        },
        chart: {
            title: 'Company Performance',
            subtitle: 'Sales, Expenses, and Profit: 2014-2017',
        },
        backgroundColor: 'transparent',
        hAxis: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" },
            slantedText: props.type === 'ColumnChart',
            slantedTextAngle: props.type === 'ColumnChart' ? 90 : 0
        },
        vAxis : {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        },
        legend: {
            textStyle: { color: themeService.current.value.dark ? "white" : "black" }
        }
    };

    const topMasteries = playerMasteryService.playerMasteryState.playerMastery.slice(0, 20);

    const chartData = [
        ['Champion', 'Points'],
    ]

    topMasteries.forEach((m) => {
        const championPoints = Number(m.championPoints);
        const championName = String(lolChampsService.lolChampState.data[m.championId] as unknown as string);

        chartData.push([championName, championPoints])
    })
</script>

<style>

.chart-container {
    margin-top: 2%;
}

</style>
