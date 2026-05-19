<template>
  <div class="device-page">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索设备名称/编码" style="width:250px" clearable @clear="loadData" @keyup.enter="loadData" />
      <el-button type="primary" @click="loadData" style="background:#16a085;border:none">搜索</el-button>
      <el-button type="success" @click="openDialog(null)">新增设备</el-button>
    </div>
    <el-table :data="devices" v-loading="loading" style="width:100%;margin-top:16px" empty-text="暂无数据">
      <el-table-column prop="deviceName" label="设备名称" />
      <el-table-column prop="deviceCode" label="设备编码" width="120" />
      <el-table-column prop="deviceType" label="设备类型" width="100" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 'ONLINE' ? 'success' : 'danger'" size="small">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除该设备？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page="query.page" @current-change="handlePageChange" />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑设备' : '新增设备'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="设备名称"><el-input v-model="form.deviceName" /></el-form-item>
        <el-form-item label="设备编码"><el-input v-model="form.deviceCode" :disabled="isEdit" /></el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="form.deviceType" style="width:100%">
            <el-option label="传感器" value="传感器" />
            <el-option label="执行器" value="执行器" />
            <el-option label="网关" value="网关" />
            <el-option label="摄像头" value="摄像头" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" style="background:#16a085;border:none">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { device } from '../api/index'

const devices = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const query = reactive({ page: 1, size: 10, keyword: '' })
const form = reactive({ deviceName: '', deviceCode: '', deviceType: '传感器', location: '', description: '' })

async function loadData() {
  loading.value = true
  try {
    const res = await device.list(query)
    devices.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function handlePageChange(page) {
  query.page = page
  loadData()
}

function openDialog(row) {
  isEdit.value = !!row
  editId.value = row?.id || null
  Object.assign(form, row || { deviceName: '', deviceCode: '', deviceType: '传感器', location: '', description: '' })
  dialogVisible.value = true
}

async function handleSubmit() {
  if (isEdit.value) {
    await device.update(editId.value, form)
    ElMessage.success('更新成功')
  } else {
    await device.add(form)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

async function handleDelete(id) {
  await device.delete(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar { display: flex; gap: 10px; align-items: center; }
</style>
