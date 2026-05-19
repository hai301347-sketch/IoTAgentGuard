<template>
  <div class="monitor-page">
    <el-row :gutter="16">
      <el-col :span="8" v-for="d in devices" :key="d.id">
        <div class="device-card">
          <div class="card-header">
            <span class="name">{{ d.deviceName }}</span>
            <span class="dot" :class="{ online: d.status === 'ONLINE' }"></span>
          </div>
          <div class="card-code">{{ d.deviceCode }} | {{ d.location }}</div>
          <div v-if="latestData[d.id]" class="data-grid">
            <div class="data-item">
              <div class="data-val" :style="{color: tempColor(latestData[d.id].temperature)}">
                {{ latestData[d.id].temperature }}°C
              </div>
              <div class="data-label">温度</div>
            </div>
            <div class="data-item">
              <div class="data-val" style="color:#3498db">{{ latestData[d.id].humidity }}%</div>
              <div class="data-label">湿度</div>
            </div>
            <div class="data-item">
              <div class="data-val" style="color:#f39c12">{{ latestData[d.id].light }} lux</div>
              <div class="data-label">光照</div>
            </div>
            <div class="data-item">
              <div class="data-val" :style="{color: smokeColor(latestData[d.id].smoke)}">
                {{ latestData[d.id].smoke }}
              </div>
              <div class="data-label">烟雾</div>
            </div>
          </div>
          <div v-else class="no-data">暂无数据</div>
        </div>
      </el-col>
    </el-row>

    <div class="agent-panel">
      <h3>AI Agent 控制面板</h3>
      <div class="agent-actions">
        <el-button type="success" @click="startSimulator">启动数据模拟器</el-button>
        <el-button type="danger" @click="stopSimulator">停止数据模拟器</el-button>
        <el-input-number v-model="interval" :min="1" :max="60" style="margin:0 10px" />
        <el-button @click="setInterval">设置频率(秒)</el-button>
      </div>
      <div class="agent-command">
        <el-input v-model="command" placeholder="输入自然语言指令，如：设置采集频率为5秒、系统巡检" @keyup.enter="executeCommand">
          <template #append><el-button @click="executeCommand">执行</el-button></template>
        </el-input>
        <div v-if="commandResult" class="command-result">
          <pre>{{ commandResult }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { device, dataApi, agentApi } from '../api/index'

const devices = ref([])
const latestData = ref({})
const command = ref('')
const commandResult = ref('')
const interval = ref(10)
let timer = null

function tempColor(t) {
  if (t > 35) return '#e74c3c'
  if (t > 25) return '#e67e22'
  return '#16a085'
}
function smokeColor(s) {
  if (s > 50) return '#e74c3c'
  if (s > 30) return '#e67e22'
  return '#a0a0b0'
}

async function loadDevices() {
  const res = await device.list({ page: 1, size: 100 })
  devices.value = res.data.list
  loadLatestData()
}

async function loadLatestData() {
  for (const d of devices.value) {
    try {
      const res = await dataApi.getLatest(d.id)
      if (res.data) {
        latestData.value[d.id] = res.data
      }
    } catch (e) {}
  }
}

async function startSimulator() {
  await agentApi.startSimulator()
  ElMessage.success('模拟器已启动')
}
async function stopSimulator() {
  await agentApi.stopSimulator()
  ElMessage.success('模拟器已停止')
}
async function setInterval() {
  await agentApi.setInterval(interval.value)
  ElMessage.success('频率已设置为' + interval.value + '秒')
}
async function executeCommand() {
  if (!command.value.trim()) return
  try {
    const res = await agentApi.query(command.value)
    commandResult.value = res.data
    command.value = ''
  } catch (e) {}
}

onMounted(() => {
  loadDevices()
  timer = setInterval(loadLatestData, 10000)
})
onUnmounted(() => clearInterval(timer))
</script>

<style scoped>
.device-card {
  background: #16213e;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid #2a2a4a;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-header .name { color: #e0e0e0; font-size: 16px; font-weight: bold; }
.dot { width: 10px; height: 10px; border-radius: 50%; background: #e74c3c; }
.dot.online { background: #16a085; box-shadow: 0 0 6px #16a085; }
.card-code { color: #a0a0b0; font-size: 12px; margin: 8px 0; }
.data-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; text-align: center; }
.data-val { font-size: 18px; font-weight: bold; }
.data-label { font-size: 11px; color: #a0a0b0; margin-top: 4px; }
.no-data { color: #a0a0b0; text-align: center; padding: 20px; }
.agent-panel {
  background: #16213e;
  border-radius: 8px;
  padding: 20px;
  margin-top: 16px;
  border: 1px solid #2a2a4a;
}
.agent-panel h3 { color: #16a085; margin-bottom: 16px; }
.agent-actions { display: flex; align-items: center; margin-bottom: 16px; }
.agent-command { margin-top: 10px; }
.command-result {
  margin-top: 12px;
  background: #0f0f1a;
  border-radius: 6px;
  padding: 12px;
  max-height: 300px;
  overflow-y: auto;
}
.command-result pre { color: #16a085; white-space: pre-wrap; margin: 0; font-size: 13px; }
</style>
