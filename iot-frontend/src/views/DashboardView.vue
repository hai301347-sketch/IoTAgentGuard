<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <div class="stat-card" :style="{borderTop: '3px solid ' + card.color}">
          <div class="stat-value" :style="{color: card.color}">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <div class="chart-card">
          <h4>今日温度趋势 (°C)</h4>
          <div ref="tempChart" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <h4>今日湿度趋势 (%)</h4>
          <div ref="humChart" style="height:300px"></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <div class="chart-card">
          <h4>最近告警</h4>
          <el-table :data="recentAlerts" style="width:100%" size="small" empty-text="暂无告警">
            <el-table-column prop="deviceCode" label="设备" width="100" />
            <el-table-column prop="alertType" label="类型" width="120" />
            <el-table-column prop="alertLevel" label="级别" width="100">
              <template #default="{row}">
                <el-tag :type="levelType(row.alertLevel)" size="small">{{ row.alertLevel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertContent" label="内容" />
            <el-table-column prop="createTime" label="时间" width="180" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { dashboard } from '../api/index'

const statCards = ref([
  { label: '设备总数', value: 0, color: '#3498db' },
  { label: '在线设备', value: 0, color: '#16a085' },
  { label: '今日数据量', value: 0, color: '#e67e22' },
  { label: '未处理告警', value: 0, color: '#e74c3c' }
])
const recentAlerts = ref([])
const tempChart = ref(null)
const humChart = ref(null)
let tempChartInstance = null
let humChartInstance = null
let timer = null

function levelType(level) {
  if (level === 'CRITICAL') return 'danger'
  if (level === 'HIGH') return 'warning'
  return 'info'
}

async function loadData() {
  try {
    const res = await dashboard.getStats()
    const d = res.data
    statCards.value[0].value = d.deviceTotal
    statCards.value[1].value = d.onlineDeviceCount
    statCards.value[2].value = d.todayDataCount
    statCards.value[3].value = d.alertCount
    recentAlerts.value = d.recentAlerts || []
    updateCharts(d.hourlyData || [])
  } catch (e) {}
}

function updateCharts(data) {
  const hours = data.map(h => h.hour + ':00')
  const temps = data.map(h => parseFloat(h.avg_temp || 0).toFixed(1))
  const hums = data.map(h => parseFloat(h.avg_humidity || 0).toFixed(1))

  const baseOption = (title, color) => ({
    tooltip: { trigger: 'axis' },
    grid: { left: 50, right: 20, top: 30, bottom: 30 },
    xAxis: { type: 'category', data: hours, axisLine: { lineStyle: { color: '#2a2a4a' } }, axisLabel: { color: '#a0a0b0' } },
    yAxis: { type: 'value', axisLine: { lineStyle: { color: '#2a2a4a' } }, axisLabel: { color: '#a0a0b0' }, splitLine: { lineStyle: { color: '#1a1a2e' } } },
    series: [{ type: 'line', data: temps, smooth: true, lineStyle: { color }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: color + '40' }, { offset: 1, color: color + '05' }]) }, itemStyle: { color } }]
  })

  tempChartInstance?.setOption(baseOption('温度', '#e74c3c'))
  baseOption.series[0].data = hums
  baseOption.series[0].lineStyle.color = '#3498db'
  baseOption.series[0].itemStyle.color = '#3498db'
  baseOption.series[0].areaStyle.color = new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#3498db40' }, { offset: 1, color: '#3498db05' }])
  humChartInstance?.setOption(baseOption('湿度', '#3498db'))
}

onMounted(() => {
  tempChartInstance = echarts.init(tempChart.value)
  humChartInstance = echarts.init(humChart.value)
  loadData()
  timer = setInterval(loadData, 30000)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  clearInterval(timer)
  tempChartInstance?.dispose()
  humChartInstance?.dispose()
  window.removeEventListener('resize', handleResize)
})

function handleResize() {
  tempChartInstance?.resize()
  humChartInstance?.resize()
}
</script>

<style scoped>
.stat-row { margin-bottom: 10px; }
.stat-card {
  background: #16213e;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
}
.stat-value { font-size: 32px; font-weight: bold; }
.stat-label { color: #a0a0b0; margin-top: 8px; font-size: 14px; }
.chart-card {
  background: #16213e;
  border-radius: 8px;
  padding: 16px;
}
.chart-card h4 { color: #e0e0e0; margin-bottom: 12px; }
</style>
