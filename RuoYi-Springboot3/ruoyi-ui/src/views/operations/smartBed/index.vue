<template>
  <div class="app-container smart-bed-page">
    <el-radio-group v-model="activeFloor" size="small" class="floor-tabs">
      <el-radio-button label="1楼">1楼</el-radio-button>
      <el-radio-button label="2楼">2楼</el-radio-button>
      <el-radio-button label="3楼">3楼</el-radio-button>
    </el-radio-group>

    <section v-for="room in rooms" :key="room.id" class="smart-room">
      <header>
        <div><strong>房间号：{{ room.name.replace('房间','') }}</strong> <span class="device-icons"><i class="el-icon-s-platform"></i><i class="el-icon-watch"></i><i class="el-icon-bell"></i></span></div>
        <div>房门状态：{{ room._payload.door }}　温度：{{ room._payload.temperature }}℃　湿度：{{ room._payload.humidity }}%　报警状态：{{ room._payload.alarm }}</div>
      </header>
      <div class="smart-beds">
        <article v-for="bed in room._payload.beds" :key="bed.bed" :class="['smart-card', { alarm: room.status === '报警' && bed.heart === 0 }]">
          <div class="card-top">
            <div>床位号：{{ bed.bed }}<br>老人姓名：{{ bed.elder || '-' }}</div>
            <span>
              <el-tooltip v-for="device in bed.devices" :key="device" :content="device" placement="top">
                <i :class="device.includes('手表') ? 'el-icon-watch' : 'el-icon-bell'"></i>
              </el-tooltip>
            </span>
          </div>
          <div class="bed-state">
            <div class="state-icon"><i class="el-icon-s-home"></i><span>{{ bed.state }}</span></div>
            <div>
              <p v-if="bed.heart !== undefined"><i class="el-icon-data-line"></i> 心率：{{ bed.heart }}次/分</p>
              <p v-if="bed.breath !== undefined"><i class="el-icon-wind-power"></i> 呼吸率：{{ bed.breath }}次/分</p>
              <p v-if="bed.leaveCount !== undefined"><i class="el-icon-timer"></i> 离床次数：{{ bed.leaveCount }}次</p>
              <p v-if="bed.leaveTime"><i class="el-icon-time"></i> 离床时间：{{ bed.leaveTime }}</p>
            </div>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script>
import { listRecords } from '@/api/nursing/operations'
import { parsePayload } from '../helpers'

export default {
  name: 'SmartBed',
  data() {
    return { activeFloor: '1楼', rooms: [] }
  },
  created() {
    listRecords('smart-bed', { pageNum: 1, pageSize: 50 }).then(res => {
      this.rooms = (res.rows || []).map(row => ({ ...row, _payload: parsePayload(row.payload) }))
    })
  }
}
</script>

<style scoped lang="scss">
.floor-tabs { margin-bottom:14px; }
.smart-room { margin-bottom:16px; padding:14px 16px; border:1px solid #e5e9f0; background:#fff; }
.smart-room header { display:flex; justify-content:space-between; margin-bottom:14px; font-size:13px; }
.device-icons { margin-left:12px; color:#1677ff; }
.device-icons i { margin-right:8px; }
.smart-beds { display:grid; grid-template-columns:repeat(3,minmax(0,1fr)); gap:14px; }
.smart-card { min-height:150px; padding:14px; background:#f7f8fa; border:1px solid #edf0f4; }
.smart-card.alarm { background:#fff0f0; border-color:#f5c2c2; }
.card-top { display:flex; justify-content:space-between; line-height:1.7; }
.card-top i { margin-left:8px; color:#1677ff; }
.bed-state { display:flex; gap:24px; align-items:center; margin-top:18px; }
.state-icon { display:flex; flex-direction:column; align-items:center; color:#667085; }
.state-icon i { font-size:30px; color:#9dbbf2; margin-bottom:5px; }
.bed-state p { margin:5px 0; color:#4b5565; }
.bed-state p i { color:#1677ff; }
@media(max-width:1000px){.smart-beds{grid-template-columns:1fr}}
</style>
