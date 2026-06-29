<template>
  <div v-loading="loading" class="app-container operations-detail">
    <template v-if="module === 'health'">
      <section class="detail-band">
        <h2>基本信息</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="老人姓名">{{ record.elderName }}</el-descriptions-item>
          <el-descriptions-item label="老人身份证号">{{ record.idCard }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ payload.birthday }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ payload.age || calculateAge(payload.birthday) }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ payload.gender }}</el-descriptions-item>
          <el-descriptions-item label="体检机构">{{ record.name }}</el-descriptions-item>
          <el-descriptions-item label="总检日期">{{ record.startDate }}</el-descriptions-item>
          <el-descriptions-item label="体检报告">
            <el-link type="primary" :underline="false">查看体检报告</el-link>
          </el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>体检总结</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="健康评分">{{ payload.score }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">{{ payload.risk }}</el-descriptions-item>
          <el-descriptions-item label="建议入住">{{ payload.advice }}</el-descriptions-item>
          <el-descriptions-item label="推荐护理等级">{{ record.productName }}</el-descriptions-item>
          <el-descriptions-item label="入住情况">{{ record.status }}</el-descriptions-item>
          <el-descriptions-item label="评估时间">{{ record.createTime }}</el-descriptions-item>
          <el-descriptions-item label="分析报告">
            <el-link type="primary" :underline="false">查看分析报告</el-link>
          </el-descriptions-item>
          <el-descriptions-item label="报告总结">{{ payload.summary }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>疾病风险</h2>
        <div class="chart-grid">
          <div ref="riskBar" class="chart"></div>
          <div ref="riskRadar" class="chart"></div>
        </div>
      </section>
      <section class="detail-band">
        <h2>异常分析</h2>
        <el-table :data="abnormalRows">
          <el-table-column label="结论" prop="conclusion" />
          <el-table-column label="项目名称" prop="item" />
          <el-table-column label="检查结果" prop="result" />
          <el-table-column label="参考值" prop="reference" />
          <el-table-column label="单位" prop="unit" />
        </el-table>
        <p class="analysis-copy"><strong>AI建议：</strong>{{ payload.summary }}</p>
      </section>
    </template>

    <template v-else-if="module === 'admission'">
      <section class="detail-band">
        <h2>基本信息</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="老人姓名">{{ record.elderName }}</el-descriptions-item>
          <el-descriptions-item label="老人身份证号">{{ record.idCard }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ payload.gender || '-' }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ payload.birthday || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系方式">{{ record.phone }}</el-descriptions-item>
          <el-descriptions-item label="家庭住址">{{ payload.address || '-' }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>家属信息</h2>
        <el-table :data="payload.family || []">
          <el-table-column label="家属姓名" prop="name" />
          <el-table-column label="联系方式" prop="phone" />
          <el-table-column label="与老人关系" prop="relation" />
        </el-table>
      </section>
      <section class="detail-band">
        <h2>入住配置</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="入住期限">{{ record.startDate }} 至 {{ record.endDate }}</el-descriptions-item>
          <el-descriptions-item label="护理等级">{{ record.productName }}</el-descriptions-item>
          <el-descriptions-item label="入住床位">{{ record.location }}</el-descriptions-item>
          <el-descriptions-item label="押金">{{ Number(payload.deposit || 0).toFixed(2) }} 元</el-descriptions-item>
          <el-descriptions-item label="床位费用">{{ Number(payload.bedFee || 0).toFixed(2) }} 元/月</el-descriptions-item>
          <el-descriptions-item label="护理费用">{{ Number(payload.nursingFee || 0).toFixed(2) }} 元/月</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>签约办理</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="合同名称">{{ payload.contractName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="入住合同">{{ payload.contractFile || '入住合同.pdf' }}</el-descriptions-item>
        </el-descriptions>
      </section>
    </template>

    <template v-else-if="module === 'checkout'">
      <section class="detail-band">
        <h2>基本信息</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="老人姓名">{{ record.elderName }}</el-descriptions-item>
          <el-descriptions-item label="退住日期">{{ record.endDate }}</el-descriptions-item>
          <el-descriptions-item label="退住原因">{{ payload.reason }}</el-descriptions-item>
          <el-descriptions-item label="老人身份证号">{{ record.idCard }}</el-descriptions-item>
          <el-descriptions-item label="入住床位">{{ record.location }}</el-descriptions-item>
          <el-descriptions-item label="护理等级">{{ record.productName }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>解除协议</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="解除日期">{{ payload.releaseDate }}</el-descriptions-item>
          <el-descriptions-item label="解除协议">{{ payload.releaseFile || '解除协议.pdf' }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band">
        <h2>费用清算</h2>
        <div class="settlement-grid">
          <div v-for="group in settlementGroups" :key="group.title" class="settlement-box">
            <h3>{{ group.title }} <span>小计：{{ sum(group.items).toFixed(2) }} 元</span></h3>
            <div v-for="(item, index) in group.items" :key="index" class="settlement-item">
              {{ item.bill || item.name }} <span>{{ Number(item.amount || 0).toFixed(2) }} 元</span>
            </div>
          </div>
        </div>
      </section>
      <section class="detail-band">
        <h2>退款凭证</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="提交人">{{ record.updateBy || '管理员' }}</el-descriptions-item>
          <el-descriptions-item label="退款方式">{{ payload.refundMethod }}</el-descriptions-item>
          <el-descriptions-item label="退款备注">{{ payload.refundRemark }}</el-descriptions-item>
          <el-descriptions-item label="退款凭证">
            <el-image v-if="payload.refundVoucher" :src="payload.refundVoucher" class="voucher" />
          </el-descriptions-item>
        </el-descriptions>
      </section>
    </template>

    <template v-else-if="module === 'device'">
      <section class="detail-band">
        <h2>设备信息</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="设备名称">{{ record.name }}</el-descriptions-item>
          <el-descriptions-item label="所属产品">{{ record.productName }}</el-descriptions-item>
          <el-descriptions-item label="DeviceSecret">{{ payload.deviceSecret }}</el-descriptions-item>
          <el-descriptions-item label="ProductKey">{{ payload.productKey }}</el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag size="mini" type="success">{{ record.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="接入位置">{{ record.location }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section class="detail-band device-data">
        <el-tabs v-model="deviceTab">
          <el-tab-pane label="设备详情" name="detail">
            <el-descriptions :column="1">
              <el-descriptions-item label="备注名称">{{ payload.alias }}</el-descriptions-item>
              <el-descriptions-item label="设备类型">{{ payload.type }}</el-descriptions-item>
              <el-descriptions-item label="地域">{{ payload.region }}</el-descriptions-item>
              <el-descriptions-item label="节点类型">{{ payload.nodeType }}</el-descriptions-item>
              <el-descriptions-item label="认证方式">{{ payload.auth }}</el-descriptions-item>
              <el-descriptions-item label="IP地址">{{ payload.ip || '-' }}</el-descriptions-item>
              <el-descriptions-item label="固件版本">{{ payload.firmware || '-' }}</el-descriptions-item>
              <el-descriptions-item label="创建人">{{ record.createBy || '-' }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ record.createTime || '-' }}</el-descriptions-item>
              <el-descriptions-item label="激活时间">{{ payload.activated }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="物模型数据" name="model">
            <el-table :data="deviceRows">
              <el-table-column type="index" label="序号" width="70" />
              <el-table-column label="标识符" prop="identifier" />
              <el-table-column label="功能名称" prop="name" />
              <el-table-column label="数据值" prop="value" />
              <el-table-column label="操作">
                <template slot-scope="scope"><el-button type="text" @click="openHistory(scope.row)">查看数据</el-button></template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </section>
    </template>

    <template v-else>
      <section class="detail-band">
        <h2>{{ detailTitle }}</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="编号">{{ record.code }}</el-descriptions-item>
          <el-descriptions-item label="名称">{{ record.name }}</el-descriptions-item>
          <el-descriptions-item label="老人姓名">{{ record.elderName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ record.idCard || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系方式">{{ record.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="位置">{{ record.location || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag size="mini" :type="statusType(record.status)">{{ record.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="期限">{{ record.startDate || '-' }} 至 {{ record.endDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ record.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </section>
      <section v-if="module === 'contract' && payload.releaseDate" class="detail-band">
        <h2>解除记录</h2>
        <el-descriptions :column="2">
          <el-descriptions-item label="解除日期">{{ payload.releaseDate }}</el-descriptions-item>
          <el-descriptions-item label="解除协议">{{ payload.releaseFile }}</el-descriptions-item>
        </el-descriptions>
      </section>
    </template>

    <div class="back-row">
      <el-button type="primary" icon="el-icon-back" @click="$router.back()">返回</el-button>
    </div>

    <el-dialog title="查看数据" :visible.sync="historyOpen" width="580px" append-to-body>
      <el-form size="small" :inline="true"><el-form-item label="时间范围"><el-select v-model="historyRange"><el-option label="1小时" value="1小时" /><el-option label="24小时" value="24小时" /><el-option label="7天" value="7天" /></el-select></el-form-item></el-form>
      <el-table :data="historyRows">
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column label="原始值" prop="value" />
        <el-table-column label="时间" prop="time" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getRecord } from '@/api/nursing/operations'
import { parsePayload, statusType } from '../helpers'

export default {
  name: 'OperationsDetail',
  data() {
    return {
      loading: false,
      record: {},
      payload: {},
      deviceTab: 'detail',
      historyOpen: false,
      historyRange: '1小时',
      historyRows: []
    }
  },
  computed: {
    module() {
      return this.$route.params.module
    },
    abnormalRows() {
      return (this.payload.abnormal || []).map(row => ({
        conclusion: row[0],
        item: row[1],
        result: row[2],
        reference: row[3],
        unit: row[4]
      }))
    },
    settlementGroups() {
      return [
        { title: '应退', items: this.payload.receivable || [] },
        { title: '欠费', items: this.payload.arrears || [] },
        { title: '余额', items: this.payload.balance || [] },
        { title: '未缴', items: this.payload.unpaid || [] }
      ]
    },
    deviceRows() {
      return this.payload.properties || this.payload.services || []
    },
    detailTitle() {
      const map = { admission: '入住详情', contract: '合同详情' }
      return map[this.module] || '业务详情'
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    statusType,
    loadData() {
      this.loading = true
      getRecord(this.module, this.$route.params.id).then(res => {
        this.record = res.data || {}
        this.payload = parsePayload(this.record.payload)
        this.$nextTick(this.renderCharts)
      }).finally(() => {
        this.loading = false
      })
    },
    renderCharts() {
      if (this.module !== 'health' || !this.$refs.riskBar) return
      const bar = echarts.init(this.$refs.riskBar)
      const values = this.payload.systems || [62, 65, 72, 75, 77, 78]
      bar.setOption({
        animation: false,
        title: { text: '不同年龄人群健康指数分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { bottom: 8, data: ['健康', '提示', '风险', '危险'] },
        grid: { left: 48, right: 24, top: 52, bottom: 58 },
        xAxis: { type: 'category', data: ['20岁', '30岁', '40岁', '50岁', '60岁', '70岁', '80岁', '90岁+'] },
        yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
        series: [
          { name: '健康', type: 'bar', stack: 'risk', barWidth: 18, data: [90, 86, 84, 65, 54, 34, 16, 5], itemStyle: { color: '#67c23a' } },
          { name: '提示', type: 'bar', stack: 'risk', barWidth: 18, data: [6, 8, 9, 20, 25, 30, 20, 20], itemStyle: { color: '#fadb14' } },
          { name: '风险', type: 'bar', stack: 'risk', barWidth: 18, data: [3, 4, 5, 8, 12, 25, 54, 61], itemStyle: { color: '#fa8c16' } },
          { name: '危险', type: 'bar', stack: 'risk', barWidth: 18, data: [1, 2, 2, 7, 9, 11, 10, 14], itemStyle: { color: '#f56c6c' } }
        ]
      })
      const radar = echarts.init(this.$refs.riskRadar)
      const score = Number(this.payload.score || this.record.amount || 0)
      radar.setOption({
        animation: false,
        title: { text: '综合健康风险', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: {},
        radar: {
          center: ['50%', '54%'],
          radius: '62%',
          splitNumber: 5,
          indicator: ['消化', '内分泌', '免疫', '循环', '泌尿', '运动'].map(name => ({ name, max: 100 }))
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '48%',
          style: {
            text: `${score.toFixed(1)}分`,
            fill: '#25324b',
            font: '600 28px sans-serif'
          }
        }],
        series: [{
          type: 'radar',
          symbolSize: 6,
          lineStyle: { color: '#67c23a', width: 2 },
          itemStyle: { color: '#67c23a' },
          data: [{
            value: values,
            areaStyle: { color: '#95de64', opacity: 0.58 }
          }]
        }]
      })
    },
    calculateAge(birthday) {
      if (!birthday) return '-'
      const birth = new Date(birthday)
      const now = new Date()
      let age = now.getFullYear() - birth.getFullYear()
      if (
        now.getMonth() < birth.getMonth() ||
        (now.getMonth() === birth.getMonth() && now.getDate() < birth.getDate())
      ) {
        age -= 1
      }
      return age
    },
    sum(items) {
      return (items || []).reduce((total, item) => total + Number(item.amount || 0), 0)
    },
    openHistory(row) {
      this.historyRows = Array.from({ length: 5 }, (_, index) => ({
        value: row.value || '92.5kW·h',
        time: `2026-06-25 ${String(15 - index).padStart(2, '0')}:00:00`
      }))
      this.historyOpen = true
    }
  }
}
</script>

<style scoped lang="scss">
.operations-detail {
  min-height: calc(100vh - 84px);
  background: #f2f4f7;
}
.detail-band {
  margin-bottom: 14px;
  padding: 18px 24px;
  background: #fff;
  border: 1px solid #e2e6ed;
  h2 {
    margin: 0 0 20px;
    font-size: 17px;
    font-weight: 500;
  }
}
.chart-grid,
.settlement-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}
.chart {
  height: 330px;
  border: 1px solid #edf0f5;
}
.settlement-box {
  min-height: 170px;
  padding: 12px;
  border: 1px solid #d9dee7;
  h3 {
    margin: 0 0 12px;
    font-size: 15px;
    span { float: right; font-weight: 400; color: #667085; }
  }
}
.settlement-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 12px;
  background: #fafbfc;
}
.voucher {
  width: 150px;
  height: 90px;
}
.analysis-copy { line-height: 1.8; }
.back-row { padding: 10px 0 24px; text-align: center; }
::v-deep .el-descriptions-item__label { width: 130px; color: #738096; }
@media (max-width: 900px) {
  .chart-grid,
  .settlement-grid { grid-template-columns: 1fr; }
}
</style>
