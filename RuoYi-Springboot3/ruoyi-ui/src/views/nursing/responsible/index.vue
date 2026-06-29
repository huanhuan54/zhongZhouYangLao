<template>
  <div class="app-container responsible-page">
    <div class="page-tools">
      <el-radio-group v-model="query.floorNo" size="small" @change="loadResidents">
        <el-radio-button :label="1">1楼</el-radio-button>
        <el-radio-button :label="2">2楼</el-radio-button>
        <el-radio-button :label="3">3楼</el-radio-button>
      </el-radio-group>
      <div class="tools-right">
        <el-input
          v-model="query.elderName"
          placeholder="搜索老人姓名"
          clearable
          size="small"
          prefix-icon="el-icon-search"
          @keyup.enter.native="loadResidents"
          @clear="loadResidents"
        />
        <el-button type="primary" size="small" icon="el-icon-user-solid" @click="openBatchAssign">
          批量设置护理员
        </el-button>
      </div>
    </div>

    <div v-loading="loading" class="room-list">
      <section v-for="room in roomGroups" :key="room.roomNo" class="room-section">
        <div class="room-header">
          <span>房间号：{{ room.roomNo }}</span>
          <el-button type="primary" size="mini" plain icon="el-icon-user" @click="openRoomAssign(room)">
            批量设置护理员
          </el-button>
        </div>
        <div class="bed-grid">
          <article v-for="resident in room.residents" :key="resident.id" class="bed-item">
            <button class="assign-link" type="button" @click="openSingleAssign(resident)">设置护理员</button>
            <div class="bed-number">床位号：{{ resident.bedNo }}</div>
            <div class="elder-row">
              <span class="elder-name">老人姓名：{{ resident.elderName }}</span>
              <el-tag size="mini" effect="plain">{{ resident.nursingLevel }}</el-tag>
            </div>
            <div class="caregiver-row">
              <span>护理员姓名：</span>
              <span v-if="resident.caregiverNames" class="caregiver-tags">{{ resident.caregiverNames }}</span>
              <span v-else class="empty-caregiver">当前床位没有安排护理员</span>
            </div>
          </article>
        </div>
      </section>
      <el-empty v-if="!loading && roomGroups.length === 0" description="暂无符合条件的老人" />
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="assignOpen" width="520px" append-to-body>
      <el-form label-width="110px">
        <el-form-item label="护理员姓名" required>
          <el-select
            v-model="selectedCaregivers"
            multiple
            :multiple-limit="4"
            placeholder="最多选择4个护理员"
            style="width: 100%"
          >
            <el-option
              v-for="item in caregivers"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
              <span>{{ item.name }}</span>
              <span class="option-phone">{{ item.phone }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <div class="assign-tip">新的设置会覆盖所选老人原有护理员安排。</div>
      </el-form>
      <div slot="footer">
        <el-button @click="assignOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmAssign">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResponsible, listCaregivers, assignCaregivers } from '@/api/nursing/responsible'

export default {
  name: 'ResponsibleElder',
  data() {
    return {
      loading: false,
      residents: [],
      caregivers: [],
      query: {
        floorNo: 1,
        elderName: ''
      },
      assignOpen: false,
      dialogTitle: '设置护理员',
      targetResidentIds: [],
      selectedCaregivers: []
    }
  },
  computed: {
    roomGroups() {
      const groups = {}
      this.residents.forEach(item => {
        if (!groups[item.roomNo]) {
          groups[item.roomNo] = { roomNo: item.roomNo, residents: [] }
        }
        groups[item.roomNo].residents.push(item)
      })
      return Object.values(groups)
    }
  },
  created() {
    this.loadResidents()
    listCaregivers().then(res => {
      this.caregivers = res.data
    })
  },
  methods: {
    loadResidents() {
      this.loading = true
      listResponsible(this.query).then(res => {
        this.residents = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    selectedIdsFromNames(names) {
      if (!names) return []
      const nameList = names.split('、')
      return this.caregivers.filter(item => nameList.includes(item.name)).map(item => item.id)
    },
    openSingleAssign(resident) {
      this.dialogTitle = `设置护理员 - ${resident.elderName}`
      this.targetResidentIds = [resident.id]
      this.selectedCaregivers = this.selectedIdsFromNames(resident.caregiverNames)
      this.assignOpen = true
    },
    openRoomAssign(room) {
      this.dialogTitle = `批量设置护理员 - ${room.roomNo}房间`
      this.targetResidentIds = room.residents.map(item => item.id)
      this.selectedCaregivers = []
      this.assignOpen = true
    },
    openBatchAssign() {
      this.dialogTitle = `批量设置护理员 - ${this.query.floorNo}楼`
      this.targetResidentIds = this.residents.map(item => item.id)
      this.selectedCaregivers = []
      this.assignOpen = true
    },
    confirmAssign() {
      if (!this.targetResidentIds.length) {
        this.$modal.msgWarning('当前没有可设置的老人')
        return
      }
      if (!this.selectedCaregivers.length) {
        this.$modal.msgWarning('请至少选择1名护理员')
        return
      }
      assignCaregivers({
        residentIds: this.targetResidentIds,
        caregiverIds: this.selectedCaregivers
      }).then(() => {
        this.$modal.msgSuccess('护理员设置成功')
        this.assignOpen = false
        this.loadResidents()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.responsible-page {
  background: #f5f7fa;
  min-height: calc(100vh - 50px);
  .page-tools {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 14px 16px;
    margin: -20px -20px 18px;
    background: #fff;
    border-bottom: 1px solid #ebeef5;
  }
  .tools-right {
    display: flex;
    gap: 10px;
    .el-input {
      width: 210px;
    }
  }
  .room-section {
    margin-bottom: 16px;
    background: #fff;
    border: 1px solid #e4e7ed;
  }
  .room-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    font-weight: 600;
    border-bottom: 1px solid #ebeef5;
  }
  .bed-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    padding: 12px;
  }
  .bed-item {
    position: relative;
    min-height: 130px;
    padding: 16px;
    background: #f7f8fa;
    border-left: 3px solid #409eff;
  }
  .assign-link {
    position: absolute;
    right: 14px;
    top: 12px;
    padding: 0;
    color: #409eff;
    border: 0;
    background: transparent;
    cursor: pointer;
  }
  .bed-number {
    margin-bottom: 14px;
    color: #606266;
  }
  .elder-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
  }
  .elder-name {
    font-weight: 600;
    color: #303133;
  }
  .caregiver-row {
    display: flex;
    align-items: flex-start;
    line-height: 22px;
    color: #606266;
  }
  .caregiver-tags {
    color: #2563eb;
  }
  .empty-caregiver {
    color: #909399;
  }
  .assign-tip {
    margin-left: 110px;
    color: #909399;
    font-size: 12px;
  }
  .option-phone {
    float: right;
    color: #909399;
    font-size: 12px;
  }
}

@media (max-width: 1100px) {
  .responsible-page .bed-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
