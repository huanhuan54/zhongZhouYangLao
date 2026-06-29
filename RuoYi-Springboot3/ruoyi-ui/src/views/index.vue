<template>
  <div class="dashboard-page">
    <section class="overview">
      <header class="section-heading">
        <div>
          <h2>数据概览</h2>
          <span>数据更新：{{ today }}</span>
        </div>
      </header>
      <div class="metric-grid">
        <div v-for="item in metrics" :key="item.label" class="metric-item">
          <div class="metric-ring" :style="{ borderColor: item.color }">
            <strong>{{ item.value }}</strong>
            <span>{{ item.unit }}</span>
          </div>
          <p>{{ item.label }}</p>
        </div>
      </div>
    </section>

    <section class="profile">
      <header class="section-heading"><h2>我的信息</h2></header>
      <div class="profile-main">
        <div class="profile-avatar"><i class="el-icon-user-solid" /></div>
        <div>
          <p>您好，<strong>管理员</strong>，今天也元气满满的一天！</p>
          <div class="profile-meta">
            <span><i class="el-icon-phone-outline" /> 138****0405</span>
            <span><i class="el-icon-office-building" /> 院长办公室</span>
            <span><i class="el-icon-medal" /> 超级管理员</span>
          </div>
        </div>
      </div>
      <div class="team-row">
        <span>下属员工</span>
        <div v-for="n in 5" :key="n" class="team-avatar"><i class="el-icon-user" /></div>
        <span class="team-more">+3</span>
      </div>
    </section>

    <section class="trend">
      <header class="section-heading">
        <h2>数据统计</h2>
        <el-radio-group v-model="activeTrend" size="mini" @change="renderTrend">
          <el-radio-button label="收入情况" />
          <el-radio-button label="入退情况" />
          <el-radio-button label="服务情况" />
        </el-radio-group>
      </header>
      <div ref="trendChart" class="chart trend-chart" />
    </section>

    <section class="quick">
      <header class="section-heading"><h2>常用功能</h2></header>
      <div class="quick-grid">
        <button v-for="item in quickLinks" :key="item.label" type="button" @click="$router.push(item.path)">
          <span class="quick-icon"><i :class="item.icon" /></span>
          <span>{{ item.label }}</span>
        </button>
      </div>
    </section>

    <section class="level-chart">
      <header class="section-heading"><h2>老人等级分布</h2><span>数据更新：{{ today }}</span></header>
      <div ref="levelChart" class="chart small-chart" />
    </section>

    <section class="age-chart">
      <header class="section-heading"><h2>老人年龄分布</h2><span>数据更新：{{ today }}</span></header>
      <div ref="ageChart" class="chart small-chart" />
    </section>

    <section class="appointment-summary">
      <header class="section-heading">
        <h2>预约总览</h2>
        <el-button type="text" @click="$router.push('/visit/appointment')">查看全部</el-button>
      </header>
      <div class="appointment-date">
        <strong>{{ todayShort }}</strong>
        <span>今日待上门 {{ summary.todayAppointments || 0 }} 人</span>
      </div>
      <div class="timeline-item">
        <span class="timeline-time">09:00</span>
        <div>
          <el-tag size="mini" type="warning">探访</el-tag>
          <p>预约人姓名：余佳俊</p>
        </div>
      </div>
      <div class="timeline-item">
        <span class="timeline-time">10:30</span>
        <div>
          <el-tag size="mini">参观</el-tag>
          <p>预约人姓名：高启强</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getDashboardSummary } from '@/api/nursing/responsible'

export default {
  name: 'Index',
  data() {
    return {
      summary: {},
      activeTrend: '服务情况',
      trendInstance: null,
      levelInstance: null,
      ageInstance: null,
      quickLinks: [
        { label: '预约登记', path: '/visit/appointment', icon: 'el-icon-date' },
        { label: '来访登记', path: '/visit/record', icon: 'el-icon-document' },
        { label: '护理项目', path: '/nursing/project', icon: 'el-icon-s-order' },
        { label: '护理计划', path: '/nursing/plan', icon: 'el-icon-document-checked' },
        { label: '护理等级', path: '/nursing/level', icon: 'el-icon-star-off' },
        { label: '负责老人', path: '/nursing/responsible', icon: 'el-icon-user-solid' },
        { label: '任务安排', path: '/nursing/task', icon: 'el-icon-s-claim' },
        { label: '智能问答', path: '/aiconsult/virtualagent', icon: 'el-icon-service' }
      ]
    }
  },
  computed: {
    ...mapGetters(['name']),
    today() {
      const d = new Date()
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },
    todayShort() {
      const d = new Date()
      return `${d.getMonth() + 1}月${d.getDate()}日`
    },
    metrics() {
      return [
        { label: '老人数量', value: this.summary.elderCount || 0, unit: '人', color: '#7db7ff' },
        { label: '床位数量', value: this.summary.bedCount || 0, unit: '张', color: '#83d7a7' },
        { label: '服务数量', value: this.summary.serviceCount || 0, unit: '项', color: '#8899e8' },
        { label: '员工数量', value: this.summary.employeeCount || 0, unit: '人', color: '#f3c76b' },
        { label: '今日来访', value: this.summary.todayVisits || 0, unit: '人', color: '#ef9297' }
      ]
    }
  },
  mounted() {
    getDashboardSummary().then(res => {
      this.summary = res.data || {}
    })
    this.$nextTick(() => {
      this.renderTrend()
      this.renderLevel()
      this.renderAge()
    })
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
  },
  methods: {
    renderTrend() {
      if (!this.$refs.trendChart) return
      this.trendInstance = this.trendInstance || echarts.init(this.$refs.trendChart)
      const seriesMap = {
        收入情况: [18, 25, 32, 28, 44, 38, 52],
        入退情况: [12, 17, 15, 24, 20, 28, 26],
        服务情况: [28, 36, 24, 62, 31, 15, 22]
      }
      this.trendInstance.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 38, right: 20, top: 25, bottom: 30 },
        xAxis: { type: 'category', data: ['06-19', '06-20', '06-21', '06-22', '06-23', '06-24', '06-25'], boundaryGap: false },
        yAxis: { type: 'value', splitLine: { lineStyle: { color: '#edf0f5' } } },
        series: [{
          type: 'line',
          smooth: true,
          symbol: 'none',
          data: seriesMap[this.activeTrend],
          lineStyle: { color: '#1677ff', width: 2 },
          areaStyle: { color: 'rgba(22,119,255,0.16)' }
        }]
      })
    },
    renderLevel() {
      this.levelInstance = echarts.init(this.$refs.levelChart)
      this.levelInstance.setOption({
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', right: 10, top: 'center' },
        series: [{
          type: 'pie',
          radius: ['0%', '66%'],
          center: ['35%', '52%'],
          data: [
            { value: 5, name: '一级护理等级' },
            { value: 6, name: '二级护理等级' },
            { value: 4, name: '三级护理等级' }
          ],
          color: ['#1677ff', '#72c75c', '#f7c65d']
        }]
      })
    },
    renderAge() {
      this.ageInstance = echarts.init(this.$refs.ageChart)
      this.ageInstance.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 34, right: 12, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: ['60以下', '60-70', '71-80', '81-90', '90以上'] },
        yAxis: { type: 'value', splitLine: { show: false } },
        series: [{
          type: 'bar',
          barWidth: 22,
          data: [2, 4, 7, 2, 3],
          itemStyle: { color: '#48b96b' }
        }]
      })
    },
    resizeCharts() {
      this.trendInstance && this.trendInstance.resize()
      this.levelInstance && this.levelInstance.resize()
      this.ageInstance && this.ageInstance.resize()
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-page {
  display: grid;
  grid-template-columns: 1.2fr 1.2fr 1fr;
  grid-template-rows: auto 300px 270px;
  gap: 12px;
  min-height: calc(100vh - 50px);
  padding: 14px;
  background: #eef1f5;
  color: #303133;
}
.dashboard-page > section {
  min-width: 0;
  padding: 16px;
  background: #fff;
  border: 1px solid #e4e7ed;
}
.overview {
  grid-column: 1 / 3;
}
.profile {
  grid-column: 3;
}
.trend {
  grid-column: 1 / 3;
}
.quick {
  grid-column: 3;
}
.level-chart {
  grid-column: 1;
}
.age-chart {
  grid-column: 2;
}
.appointment-summary {
  grid-column: 3;
}
.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 28px;
  margin-bottom: 12px;
  h2 {
    display: inline-block;
    margin: 0;
    font-size: 15px;
    font-weight: 600;
  }
  span {
    margin-left: 12px;
    color: #909399;
    font-size: 12px;
  }
}
.metric-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  align-items: center;
  gap: 16px;
  padding: 4px 10px;
}
.metric-item {
  text-align: center;
  p {
    margin: 7px 0 0;
    color: #606266;
    font-size: 12px;
  }
}
.metric-ring {
  display: flex;
  align-items: baseline;
  justify-content: center;
  width: 72px;
  height: 72px;
  margin: 0 auto;
  border: 8px solid;
  border-radius: 50%;
  line-height: 56px;
  strong {
    font-size: 20px;
  }
  span {
    margin-left: 2px;
    color: #909399;
    font-size: 11px;
  }
}
.profile-main {
  display: flex;
  align-items: center;
  gap: 14px;
}
.profile-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 58px;
  height: 58px;
  flex: none;
  color: #fff;
  font-size: 28px;
  border-radius: 50%;
  background: #f0b849;
}
.profile-main p {
  margin: 0 0 10px;
  font-size: 13px;
}
.profile-meta {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 7px 18px;
  color: #606266;
  font-size: 12px;
}
.team-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
  color: #606266;
  font-size: 12px;
}
.team-avatar,
.team-more {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  color: #1677ff;
  border-radius: 50%;
  background: #e8f3ff;
}
.chart {
  width: 100%;
}
.trend-chart {
  height: 235px;
}
.small-chart {
  height: 205px;
}
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 18px 8px;
  padding-top: 8px;
  button {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    min-width: 0;
    padding: 0;
    color: #606266;
    font-size: 12px;
    border: 0;
    background: transparent;
    cursor: pointer;
  }
}
.quick-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 46px;
  height: 46px;
  color: #1677ff;
  font-size: 20px;
  border-radius: 50%;
  background: #e8f3ff;
}
.appointment-date {
  display: flex;
  justify-content: space-between;
  padding: 10px 12px;
  margin-bottom: 12px;
  background: #f7f8fa;
  span {
    color: #909399;
    font-size: 12px;
  }
}
.timeline-item {
  display: grid;
  grid-template-columns: 58px 1fr;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
  p {
    margin: 6px 0 0;
    color: #606266;
    font-size: 12px;
  }
}
.timeline-time {
  font-weight: 600;
}
@media (max-width: 1200px) {
  .dashboard-page {
    grid-template-columns: 1fr 1fr;
    grid-template-rows: auto;
  }
  .overview,
  .trend {
    grid-column: 1 / 3;
  }
  .profile,
  .quick,
  .level-chart,
  .age-chart,
  .appointment-summary {
    grid-column: auto;
  }
}
</style>
