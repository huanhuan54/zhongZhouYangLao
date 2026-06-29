<template>
  <div v-loading="loading" class="app-container order-detail">
    <section class="detail-section progress-section">
      <h2>订单进度</h2>
      <el-steps :active="activeStep" align-center finish-status="success">
        <el-step
          v-for="item in progressSteps"
          :key="item.title"
          :title="item.title"
          :description="item.time"
        />
      </el-steps>
    </section>

    <section class="detail-section">
      <h2>订单信息</h2>
      <el-descriptions :column="2">
        <el-descriptions-item label="订单编号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="期望服务时间">{{ order.expectedServiceTime }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ order.elderName }}</el-descriptions-item>
        <el-descriptions-item label="床位号">{{ order.bedNo }}</el-descriptions-item>
        <el-descriptions-item label="护理项目名称">{{ order.projectName }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">{{ money(order.orderAmount) }} 元</el-descriptions-item>
        <el-descriptions-item label="下单人">{{ order.orderUser }}</el-descriptions-item>
        <el-descriptions-item label="下单人手机号">{{ order.orderUserPhone }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag size="mini" effect="plain" :type="orderTagType(order.orderStatus)">
            {{ orderStatusText(order.orderStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交易状态">{{ paymentStatusText(order.paymentStatus) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ order.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="order.paymentStatus === 1" class="detail-section record-section">
      <h2>支付记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="交易状态">
          <span class="success-text">已支付</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付渠道">线上支付</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ order.payChannel || '-' }}</el-descriptions-item>
        <el-descriptions-item label="微信支付订单号">{{ order.payOrderNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付金额">{{ money(order.orderAmount) }} 元</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="order.orderStatus === 5" class="detail-section record-section">
      <h2>取消记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="取消人">{{ order.updateBy || order.orderUser }}</el-descriptions-item>
        <el-descriptions-item label="取消时间">{{ order.cancelTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消原因">{{ order.cancelReason || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="[2, 3, 4].includes(order.orderStatus)" class="detail-section record-section">
      <h2>执行记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="执行人">{{ order.caregiverName || order.updateBy || '护理员' }}</el-descriptions-item>
        <el-descriptions-item label="执行时间">{{ order.executeTime || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="order.executeImage" label="执行图片">
          <el-image
            :src="order.executeImage"
            :preview-src-list="[order.executeImage]"
            fit="cover"
            class="record-image"
          />
        </el-descriptions-item>
        <el-descriptions-item label="执行记录">{{ order.executeRecord || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="order.refundStatus > 0" class="detail-section record-section">
      <h2>退款记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="退款状态">
          <span :class="refundTextClass(order.refundStatus)">{{ refundStatusText(order.refundStatus) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">
          {{ order.applicant || '-' }}
          <el-tag v-if="order.applicantType" size="mini" type="warning" effect="plain">{{ order.applicantType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ order.applyTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退款原因">{{ order.refundReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退款渠道">{{ order.refundChannel || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退款方式">{{ order.refundMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="order.refundTime" label="退款时间">{{ order.refundTime }}</el-descriptions-item>
        <el-descriptions-item v-if="order.refundTransactionNo" label="退款流水号">{{ order.refundTransactionNo }}</el-descriptions-item>
        <el-descriptions-item v-if="order.failureCode" label="失败状态码">{{ order.failureCode }}</el-descriptions-item>
        <el-descriptions-item v-if="order.failureReason" label="失败原因">{{ order.failureReason }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <div class="back-row">
      <el-button type="primary" icon="el-icon-back" @click="$router.back()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getOrder } from '@/api/nursing/order'

export default {
  name: 'CareOrderDetail',
  data() {
    return {
      loading: false,
      order: {}
    }
  },
  computed: {
    progressSteps() {
      const base = [{ title: '已下单', time: this.order.createTime || '' }]
      if (this.order.orderStatus === 5) {
        return base.concat([{ title: '已关闭', time: this.order.cancelTime || '' }])
      }
      if (this.order.orderStatus >= 1 || this.order.paymentStatus === 1) {
        base.push({ title: '已支付', time: this.order.payTime || '' })
      } else {
        base.push({ title: '已支付', time: '' })
      }
      if ([2, 3, 4].includes(this.order.orderStatus)) {
        base.push({ title: '已执行', time: this.order.executeTime || '' })
      } else {
        base.push({ title: '已执行', time: '' })
      }
      if (this.order.orderStatus === 4) {
        base.push({ title: '已退款', time: this.order.refundTime || '' })
      } else {
        base.push({ title: '已完成', time: this.order.orderStatus === 3 ? this.order.updateTime || '' : '' })
      }
      return base
    },
    activeStep() {
      if (this.order.orderStatus === 5) return 2
      if (this.order.orderStatus === 0) return 1
      if (this.order.orderStatus === 1) return 2
      if (this.order.orderStatus === 2) return 3
      return 4
    }
  },
  created() {
    this.loadOrder()
  },
  methods: {
    loadOrder() {
      this.loading = true
      getOrder(this.$route.params.id).then(res => {
        this.order = res.data || {}
      }).finally(() => {
        this.loading = false
      })
    },
    money(value) {
      return Number(value || 0).toFixed(2)
    },
    orderStatusText(status) {
      return ['待支付', '待执行', '已执行', '已完成', '已退款', '已关闭'][status] || '-'
    },
    orderTagType(status) {
      return ['warning', 'warning', '', 'success', 'warning', 'danger'][status] || 'info'
    },
    paymentStatusText(status) {
      return ['待支付', '已支付', '已关闭'][status] || '-'
    },
    refundStatusText(status) {
      return ['', '退款处理中', '退款成功', '退款失败'][status] || '-'
    },
    refundTextClass(status) {
      return status === 2 ? 'success-text' : status === 3 ? 'danger-text' : 'warning-text'
    }
  }
}
</script>

<style scoped lang="scss">
.order-detail {
  min-height: calc(100vh - 84px);
  background: #f2f4f7;
}
.detail-section {
  margin-bottom: 14px;
  padding: 18px 24px;
  background: #fff;
  border: 1px solid #e2e6ed;
  h2 {
    margin: 0 0 20px;
    font-size: 17px;
    font-weight: 500;
    color: #202b3c;
  }
}
.progress-section {
  padding-bottom: 24px;
}
.record-section {
  min-height: 150px;
}
.record-image {
  width: 120px;
  height: 100px;
}
.success-text { color: #16b777; }
.warning-text { color: #e6a23c; }
.danger-text { color: #f56c6c; }
.back-row {
  padding: 8px 0 24px;
  text-align: center;
}
::v-deep .el-descriptions-item__label {
  width: 120px;
  color: #738096;
}
::v-deep .el-step__head.is-success,
::v-deep .el-step__title.is-success,
::v-deep .el-step__description.is-success {
  color: #1677ff;
  border-color: #1677ff;
}
</style>
