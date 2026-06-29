<template>
  <div class="app-container bed-page">
    <div class="bed-toolbar">
      <el-radio-group v-model="activeFloor" size="small">
        <el-radio-button v-for="floor in floors" :key="floor.id" :label="floor.id">{{ floor.name }}</el-radio-button>
      </el-radio-group>
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="openRoom">新增房间</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="openBed">新增床位</el-button>
      </div>
    </div>

    <div class="bed-legend">
      <span><svg-icon icon-class="bed-status" class-name="bed-icon empty" />空闲</span>
      <span><svg-icon icon-class="bed-status" class-name="bed-icon occupied" />已入住</span>
      <span><svg-icon icon-class="bed-status" class-name="bed-icon pending" />请假中</span>
    </div>

    <div v-if="currentFloor" class="room-grid">
      <section v-for="room in currentPayload.rooms" :key="room.room" class="room-panel">
        <header>
          <strong>房间号：{{ room.room }}</strong>
          <span>
            <el-button type="text" @click="editRoom(room)">编辑</el-button>
            <el-button type="text" class="danger-link" @click="deleteRoom(room)">删除</el-button>
          </span>
        </header>
        <div class="beds">
          <div v-for="bed in room.beds" :key="bed.bed" class="bed-item">
            <svg-icon icon-class="bed-status" :class-name="`bed-icon ${bed.status}`" />
            <div>
              <p>床位号：{{ bed.bed }}</p>
              <p>老人姓名：{{ bed.elder || '未安排' }}</p>
            </div>
          </div>
        </div>
      </section>
    </div>

    <el-dialog :title="roomForm.editing ? '编辑房间' : '新增房间'" :visible.sync="roomOpen" :width="roomForm.editing ? '680px' : '440px'" append-to-body>
      <el-form label-width="90px">
        <el-form-item label="房间号"><el-input v-model="roomForm.room" maxlength="5" /></el-form-item>
        <template v-if="roomForm.editing">
          <div class="bed-status-title">床位状态</div>
          <div v-for="bed in roomForm.beds" :key="bed.bed" class="bed-status-row">
            <div class="bed-status-name">
              <svg-icon icon-class="bed-status" :class-name="`bed-row-icon ${bed.status}`" />
              <span>{{ bed.bed }}</span>
            </div>
            <el-select v-model="bed.status" size="small" @change="handleBedStatusChange(bed)">
              <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value" />
            </el-select>
            <el-input
              v-model="bed.elder"
              size="small"
              :disabled="bed.status === 'empty'"
              :placeholder="bed.status === 'empty' ? '空闲床位' : '老人姓名'"
            />
          </div>
        </template>
      </el-form>
      <div slot="footer"><el-button @click="roomOpen=false">取消</el-button><el-button type="primary" @click="saveRoom">确定</el-button></div>
    </el-dialog>

    <el-dialog title="新增床位" :visible.sync="bedOpen" width="460px" append-to-body>
      <el-form label-width="90px">
        <el-form-item label="房间号"><el-select v-model="bedForm.room" style="width:100%"><el-option v-for="room in currentPayload.rooms" :key="room.room" :label="room.room" :value="room.room" /></el-select></el-form-item>
        <el-form-item label="床位名称"><el-input v-model="bedForm.bed" /></el-form-item>
        <el-form-item label="床位状态">
          <el-select v-model="bedForm.status" style="width:100%" @change="handleBedStatusChange(bedForm)">
            <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="老人姓名"><el-input v-model="bedForm.elder" :disabled="bedForm.status === 'empty'" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="bedOpen=false">取消</el-button><el-button type="primary" @click="saveBed">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecords, updateRecord } from '@/api/nursing/operations'
import { parsePayload, serializePayload } from '../helpers'

export default {
  name: 'BedPreview',
  data() {
    return {
      floors: [],
      activeFloor: null,
      roomOpen: false,
      bedOpen: false,
      roomForm: {},
      bedForm: {},
      statusOptions: [
        { label: '空闲', value: 'empty' },
        { label: '已入住', value: 'occupied' },
        { label: '请假中', value: 'pending' }
      ]
    }
  },
  computed: {
    currentFloor() {
      return this.floors.find(item => item.id === this.activeFloor)
    },
    currentPayload() {
      return this.currentFloor ? this.currentFloor._payload : { rooms: [] }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      listRecords('bed', { pageNum: 1, pageSize: 50 }).then(res => {
        this.floors = (res.rows || []).map(row => ({ ...row, _payload: parsePayload(row.payload) }))
        if (!this.activeFloor && this.floors.length) this.activeFloor = this.floors[0].id
      })
    },
    persist() {
      const row = this.currentFloor
      return updateRecord('bed', { ...row, payload: serializePayload(row._payload) }).then(() => {
        this.$modal.msgSuccess('床位数据已更新')
        this.load()
      })
    },
    openRoom() {
      this.roomForm = { room: '', editing: false }
      this.roomOpen = true
    },
    editRoom(room) {
      this.roomForm = {
        room: room.room,
        original: room.room,
        editing: true,
        beds: room.beds.map(bed => ({ ...bed }))
      }
      this.roomOpen = true
    },
    saveRoom() {
      if (!this.roomForm.room) return
      if (this.roomForm.editing) {
        const invalidBed = this.roomForm.beds.find(bed => bed.status !== 'empty' && !bed.elder)
        if (invalidBed) {
          this.$modal.msgWarning(`请填写床位 ${invalidBed.bed} 的老人姓名`)
          return
        }
        const room = this.currentPayload.rooms.find(item => item.room === this.roomForm.original)
        room.room = this.roomForm.room
        room.beds = this.roomForm.beds.map(bed => ({
          ...bed,
          elder: bed.status === 'empty' ? '' : bed.elder
        }))
      } else {
        this.currentPayload.rooms.push({ room: this.roomForm.room, beds: [] })
      }
      this.roomOpen = false
      this.persist()
    },
    deleteRoom(room) {
      this.$modal.confirm(`确认删除房间 ${room.room} 吗？`).then(() => {
        this.currentPayload.rooms = this.currentPayload.rooms.filter(item => item !== room)
        return this.persist()
      })
    },
    openBed() {
      this.bedForm = { room: '', bed: '', elder: '', status: 'empty' }
      this.bedOpen = true
    },
    saveBed() {
      const room = this.currentPayload.rooms.find(item => item.room === this.bedForm.room)
      if (!room || !this.bedForm.bed) return
      if (this.bedForm.status !== 'empty' && !this.bedForm.elder) {
        this.$modal.msgWarning('已入住或请假中的床位需要填写老人姓名')
        return
      }
      room.beds.push({
        bed: this.bedForm.bed,
        elder: this.bedForm.status === 'empty' ? '' : this.bedForm.elder,
        status: this.bedForm.status
      })
      this.bedOpen = false
      this.persist()
    },
    handleBedStatusChange(bed) {
      if (bed.status === 'empty') bed.elder = ''
    }
  }
}
</script>

<style scoped lang="scss">
.bed-toolbar { display:flex; justify-content:space-between; align-items:center; margin-bottom:14px; }
.bed-legend { display:flex; gap:24px; padding:10px 0 16px; color:#667085; }
.bed-legend span { display:flex; align-items:center; gap:7px; }
.room-grid { display:grid; grid-template-columns:repeat(3,minmax(0,1fr)); gap:14px; }
.room-panel { padding:14px; background:#f7f8fa; border:1px solid #eceff3; }
.room-panel header { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
.beds { display:grid; grid-template-columns:repeat(2,minmax(0,1fr)); gap:10px; }
.bed-item { display:flex; align-items:center; gap:8px; min-height:64px; padding:8px; background:#fff; }
.bed-item p { margin:2px 0; font-size:13px; }
.bed-icon { width:28px; height:28px; color:#c6ccd5; flex:0 0 auto; }
.bed-icon.occupied { color:#67c23a; }
.bed-icon.pending { color:#e6a23c; }
.bed-status-title { margin:4px 0 10px; padding-top:12px; border-top:1px solid #ebeef5; color:#303133; font-weight:600; }
.bed-status-row { display:grid; grid-template-columns:72px 104px minmax(0,1fr); gap:10px; align-items:center; margin-bottom:10px; }
.bed-status-name { display:flex; align-items:center; gap:6px; color:#606266; }
.bed-row-icon { width:20px; height:20px; color:#c6ccd5; }
.bed-row-icon.occupied { color:#67c23a; }
.bed-row-icon.pending { color:#e6a23c; }
.danger-link { color:#f56c6c; }
@media(max-width:1100px){.room-grid{grid-template-columns:repeat(2,1fr)}}
</style>
