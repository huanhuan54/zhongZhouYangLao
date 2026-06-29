<template>
  <div class="app-container operation-form-page">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <section class="form-section">
        <h2>基本信息</h2>
        <div class="form-grid">
          <el-form-item v-if="usesElder" label="老人姓名" prop="elderName">
            <el-input v-model="form.elderName" maxlength="10" show-word-limit />
          </el-form-item>
          <el-form-item v-if="usesElder" label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" maxlength="18" show-word-limit />
          </el-form-item>
          <el-form-item v-if="usesElder" label="联系方式" prop="phone">
            <el-input v-model="form.phone" maxlength="11" show-word-limit />
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" maxlength="20" show-word-limit />
          </el-form-item>
          <el-form-item label="业务编号" prop="code">
            <el-input v-model="form.code" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" style="width: 100%">
              <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="module !== 'alarm-rule'" label="位置">
            <el-input v-model="form.location" />
          </el-form-item>
          <el-form-item label="产品/等级">
            <el-input v-model="form.productName" />
          </el-form-item>
        </div>
      </section>

      <section v-if="module === 'admission'" class="form-section">
        <h2>家属信息</h2>
        <el-table :data="payload.family">
          <el-table-column label="家属姓名">
            <template slot-scope="scope"><el-input v-model="scope.row.name" /></template>
          </el-table-column>
          <el-table-column label="联系方式">
            <template slot-scope="scope"><el-input v-model="scope.row.phone" maxlength="11" /></template>
          </el-table-column>
          <el-table-column label="与老人关系">
            <template slot-scope="scope">
              <el-select v-model="scope.row.relation">
                <el-option v-for="item in relations" :key="item" :label="item" :value="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" class="danger-link" @click="payload.family.splice(scope.$index, 1)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button class="add-row" icon="el-icon-plus" @click="payload.family.push({ name: '', phone: '', relation: '' })">添加家属</el-button>
      </section>

      <section v-if="module === 'admission'" class="form-section">
        <h2>入住配置</h2>
        <div class="form-grid">
          <el-form-item label="入住期限">
            <el-date-picker v-model="dateRange" type="daterange" value-format="yyyy-MM-dd" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 100%" />
          </el-form-item>
          <el-form-item label="费用期限">
            <el-date-picker v-model="feeRange" type="daterange" value-format="yyyy-MM-dd" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 100%" />
          </el-form-item>
          <el-form-item label="护理等级"><el-input v-model="form.productName" /></el-form-item>
          <el-form-item label="入住床位"><el-input v-model="form.location" /></el-form-item>
          <el-form-item label="押金"><el-input-number v-model="payload.deposit" :min="0" /></el-form-item>
          <el-form-item label="床位费用"><el-input-number v-model="payload.bedFee" :min="0" /></el-form-item>
          <el-form-item label="护理费用"><el-input-number v-model="payload.nursingFee" :min="0" /></el-form-item>
          <el-form-item label="医保费用"><el-input-number v-model="payload.medicalFee" :min="0" /></el-form-item>
          <el-form-item label="其他费用"><el-input-number v-model="payload.otherFee" :min="0" /></el-form-item>
          <el-form-item label="政府补贴"><el-input-number v-model="payload.subsidy" :min="0" /></el-form-item>
        </div>
      </section>

      <section v-if="module === 'admission'" class="form-section">
        <h2>签约办理</h2>
        <div class="form-grid">
          <el-form-item label="合同名称">
            <el-input v-model="payload.contractName" maxlength="20" show-word-limit />
          </el-form-item>
          <el-form-item label="签约日期">
            <el-date-picker v-model="payload.signDate" type="date" value-format="yyyy-MM-dd" style="width: 100%" />
          </el-form-item>
          <el-form-item label="甲方姓名">
            <el-input v-model="payload.partyA" maxlength="10" show-word-limit />
          </el-form-item>
          <el-form-item label="乙方姓名">
            <el-input v-model="payload.partyB" maxlength="10" show-word-limit />
          </el-form-item>
          <el-form-item label="双方联系方式">
            <el-input v-model="payload.partyPhone" maxlength="11" show-word-limit />
          </el-form-item>
          <el-form-item label="入住合同">
            <file-upload v-model="payload.contractFile" :limit="1" :file-size="60" :file-type="['pdf']" />
          </el-form-item>
        </div>
      </section>

      <section v-if="module === 'checkout'" class="form-section">
        <h2>退住与费用清算</h2>
        <div class="form-grid">
          <el-form-item label="退住原因">
            <el-select v-model="payload.reason" style="width: 100%">
              <el-option v-for="item in checkoutReasons" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="退住日期"><el-date-picker v-model="form.endDate" value-format="yyyy-MM-dd" style="width: 100%" /></el-form-item>
          <el-form-item label="退款方式">
            <el-select v-model="payload.refundMethod" style="width: 100%">
              <el-option label="现金" value="现金" />
              <el-option label="银行卡" value="银行卡" />
              <el-option label="微信" value="微信" />
            </el-select>
          </el-form-item>
          <el-form-item label="退款金额"><el-input-number v-model="form.amount" :min="0" /></el-form-item>
          <el-form-item label="退款备注" class="wide-item"><el-input v-model="payload.refundRemark" type="textarea" :rows="3" /></el-form-item>
        </div>
      </section>

      <section v-if="module === 'alarm-rule'" class="form-section">
        <h2>报警规则</h2>
        <div class="form-grid">
          <el-form-item label="功能名称"><el-input v-model="payload.function" /></el-form-item>
          <el-form-item label="报警数据类型">
            <el-radio-group v-model="payload.type">
              <el-radio label="老人异常报警" />
              <el-radio label="设备异常报警" />
            </el-radio-group>
          </el-form-item>
          <el-form-item label="运算符">
            <el-select v-model="payload.operator" style="width: 100%">
              <el-option label=">" value=">" /><el-option label="<" value="<" /><el-option label="=" value="=" />
            </el-select>
          </el-form-item>
          <el-form-item label="阈值"><el-input-number v-model="payload.threshold" /></el-form-item>
          <el-form-item label="持续周期"><el-input v-model="payload.duration" /></el-form-item>
          <el-form-item label="报警生效时段"><el-input v-model="payload.time" /></el-form-item>
        </div>
      </section>

      <section v-if="module === 'room-type'" class="form-section">
        <h2>房型配置</h2>
        <div class="form-grid">
          <el-form-item label="床位数量"><el-input-number v-model="payload.bedCount" :min="1" :max="20" /></el-form-item>
          <el-form-item label="房间图片"><image-upload v-model="payload.image" :limit="1" /></el-form-item>
          <el-form-item label="房型介绍" class="wide-item"><el-input v-model="payload.description" type="textarea" :rows="4" maxlength="100" show-word-limit /></el-form-item>
        </div>
      </section>

      <section v-if="module === 'device'" class="form-section">
        <h2>设备配置</h2>
        <div class="form-grid">
          <el-form-item label="备注名称"><el-input v-model="payload.alias" maxlength="15" /></el-form-item>
          <el-form-item label="设备类型">
            <el-radio-group v-model="payload.type">
              <el-radio label="随身设备" />
              <el-radio label="固定设备" />
            </el-radio-group>
          </el-form-item>
          <el-form-item label="DeviceSecret"><el-input v-model="payload.deviceSecret" /></el-form-item>
          <el-form-item label="ProductKey"><el-input v-model="payload.productKey" /></el-form-item>
        </div>
      </section>

      <section class="form-section">
        <h2>补充信息</h2>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="3" maxlength="200" show-word-limit /></el-form-item>
      </section>

      <div class="form-actions">
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button v-if="module === 'admission'" type="primary" @click="billOpen = true">账单预览</el-button>
      </div>
    </el-form>

    <el-dialog title="账单预览" :visible.sync="billOpen" width="620px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="押金">{{ money(payload.deposit) }}</el-descriptions-item>
        <el-descriptions-item label="床位费用">{{ money(payload.bedFee) }}</el-descriptions-item>
        <el-descriptions-item label="护理费用">{{ money(payload.nursingFee) }}</el-descriptions-item>
        <el-descriptions-item label="医保费用">{{ money(payload.medicalFee) }}</el-descriptions-item>
        <el-descriptions-item label="其他费用">{{ money(payload.otherFee) }}</el-descriptions-item>
        <el-descriptions-item label="政府补贴">-{{ money(payload.subsidy) }}</el-descriptions-item>
        <el-descriptions-item label="应收合计" :span="2">
          <strong class="bill-total">{{ money(billTotal) }}</strong>
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer">
        <el-button @click="billOpen = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addRecord, getRecord, updateRecord } from '@/api/nursing/operations'
import { parsePayload, serializePayload } from '../helpers'

export default {
  name: 'OperationsForm',
  data() {
    return {
      form: {
        recordType: 'business',
        name: '',
        code: '',
        status: '',
        elderName: '',
        idCard: '',
        phone: '',
        location: '',
        productName: '',
        amount: 0,
        startDate: '',
        endDate: '',
        remark: ''
      },
      payload: { family: [] },
      dateRange: [],
      feeRange: [],
      billOpen: false,
      relations: ['子女', '配偶', '亲属', '朋友'],
      checkoutReasons: ['服务不同', '身体康复回家', '转院治疗', '其他原因'],
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  computed: {
    module() {
      return this.$route.params.module
    },
    usesElder() {
      return ['admission', 'checkout'].includes(this.module)
    },
    statusOptions() {
      const map = {
        admission: ['申请中', '已入住'],
        checkout: ['申请中', '已退住'],
        'alarm-rule': ['启用', '禁用'],
        'room-type': ['启用', '禁用'],
        device: ['在线', '离线']
      }
      return map[this.module] || ['正常', '启用', '禁用']
    },
    billTotal() {
      const income = ['deposit', 'bedFee', 'nursingFee', 'medicalFee', 'otherFee']
        .reduce((total, key) => total + Number(this.payload[key] || 0), 0)
      return Math.max(0, income - Number(this.payload.subsidy || 0))
    }
  },
  created() {
    if (this.$route.params.id) this.loadData()
    else this.setupDefaults()
  },
  methods: {
    setupDefaults() {
      const typeMap = {
        admission: 'resident',
        checkout: 'settlement',
        'room-type': 'room',
        device: 'iot',
        'alarm-rule': 'rule'
      }
      this.form.recordType = typeMap[this.module] || 'business'
      this.form.status = this.statusOptions[0]
      if (this.module === 'room-type') this.payload = { family: [], bedCount: 1, image: '', description: '' }
      if (this.module === 'device') this.payload = { family: [], alias: '', type: '固定设备', deviceSecret: '', productKey: '' }
      if (this.module === 'alarm-rule') this.payload = { family: [], function: '', type: '老人异常报警', operator: '>', threshold: 0, duration: '持续1个周期', time: '00:00:00-23:59:00' }
    },
    loadData() {
      getRecord(this.module, this.$route.params.id).then(res => {
        this.form = { ...this.form, ...(res.data || {}) }
        this.payload = { family: [], ...parsePayload(this.form.payload) }
        this.dateRange = [this.form.startDate, this.form.endDate].filter(Boolean)
      })
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.dateRange.length === 2) {
          this.form.startDate = this.dateRange[0]
          this.form.endDate = this.dateRange[1]
        }
        this.form.payload = serializePayload(this.payload)
        const request = this.form.id ? updateRecord(this.module, this.form) : addRecord(this.module, this.form)
        request.then(() => {
          this.$modal.msgSuccess('提交成功')
          this.$router.back()
        })
      })
    },
    money(value) {
      return `¥${Number(value || 0).toFixed(2)}`
    }
  }
}
</script>

<style scoped lang="scss">
.operation-form-page {
  background: #f2f4f7;
}
.form-section {
  margin-bottom: 14px;
  padding: 18px 24px;
  background: #fff;
  border: 1px solid #e2e6ed;
  h2 { margin: 0 0 20px; font-size: 17px; font-weight: 500; }
}
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0 56px;
}
.wide-item { grid-column: 1 / -1; }
.add-row { margin-top: 12px; }
.danger-link { color: #f56c6c; }
.form-actions { padding: 12px 0 30px; text-align: center; }
.bill-total { color: #f56c6c; font-size: 18px; }
@media (max-width: 900px) {
  .form-grid { grid-template-columns: 1fr; }
}
</style>
