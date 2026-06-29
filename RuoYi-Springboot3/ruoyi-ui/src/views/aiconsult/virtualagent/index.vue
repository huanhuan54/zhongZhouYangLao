<template>
  <div class="app-container ai-page">
    <aside class="ai-sidebar">
      <div class="assistant-profile">
        <div class="assistant-avatar"><i class="el-icon-service" /></div>
        <div>
          <h2>小智养老</h2>
          <p>中州养老智能问答助手</p>
        </div>
      </div>
      <button
        v-for="item in suggestions"
        :key="item"
        class="suggestion"
        type="button"
        @click="askSuggestion(item)"
      >
        <i class="el-icon-chat-dot-round" />
        <span>{{ item }}</span>
      </button>
      <div class="ability-list">
        <h3>可以咨询</h3>
        <p>护理项目与收费</p>
        <p>护理计划与等级</p>
        <p>探访预约流程</p>
        <p>护理员安排</p>
      </div>
    </aside>

    <main class="chat-panel">
      <header class="chat-header">
        <div>
          <strong>养老服务咨询</strong>
          <span :class="['mode-badge', currentMode]">{{ modeText }}</span>
          <span class="api-name">API：{{ apiName }}</span>
        </div>
        <el-button type="text" icon="el-icon-delete" @click="clearMessages">清空对话</el-button>
      </header>

      <section ref="messageList" class="message-list">
        <div v-for="(message, index) in messages" :key="index" :class="['message-row', message.role]">
          <div class="message-avatar">
            <i :class="message.role === 'assistant' ? 'el-icon-service' : 'el-icon-user-solid'" />
          </div>
          <div class="message-content">
            <div class="message-name">{{ message.role === 'assistant' ? '小智' : '我' }}</div>
            <div class="message-bubble" v-html="formatContent(message.content)" />
          </div>
        </div>
        <div v-if="loading" class="message-row assistant">
          <div class="message-avatar"><i class="el-icon-service" /></div>
          <div class="message-content">
            <div class="message-name">小智</div>
            <div class="message-bubble typing">
              <span />
              <span />
              <span />
            </div>
          </div>
        </div>
      </section>

      <footer class="chat-input-area">
        <el-input
          v-model="prompt"
          type="textarea"
          :rows="3"
          maxlength="500"
          show-word-limit
          resize="none"
          placeholder="请输入养老服务相关问题，Enter发送，Shift+Enter换行"
          @keydown.native="handleKeydown"
        />
        <div class="input-actions">
          <span>智能回答仅供参考，健康问题请咨询专业医护人员。</span>
          <el-button type="primary" icon="el-icon-s-promotion" :loading="loading" @click="send">
            发送
          </el-button>
        </div>
      </footer>
    </main>
  </div>
</template>

<script>
import { sendChat } from '@/api/aiconsult/virtualagent'

export default {
  name: 'VirtualAgent',
  data() {
    return {
      prompt: '',
      loading: false,
      currentMode: 'local',
      apiName: 'DashScope',
      messages: [
        {
          role: 'assistant',
          content: '您好，我是中州养老智能助手小智。您可以咨询护理项目、护理计划、探访预约、老人照护、账单费用等问题。'
        }
      ],
      suggestions: [
        '如何预约探访老人？',
        '养老院有哪些护理项目？',
        '护理等级如何选择？',
        '怎样给老人设置护理员？'
      ]
    }
  },
  computed: {
    modeText() {
      return {
        local: '本地知识库',
        online: '大模型在线',
        fallback: '智能降级'
      }[this.currentMode] || '智能服务'
    }
  },
  methods: {
    formatContent(content) {
      return String(content || '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\n/g, '<br>')
    },
    askSuggestion(text) {
      this.prompt = text
      this.send()
    },
    handleKeydown(event) {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault()
        this.send()
      }
    },
    send() {
      const text = this.prompt.trim()
      if (!text || this.loading) return
      this.messages.push({ role: 'user', content: text })
      this.prompt = ''
      this.loading = true
      this.scrollBottom()
      sendChat(text).then(res => {
        const data = res.data || {}
        this.currentMode = data.mode || 'local'
        this.apiName = data.apiName || this.apiName
        this.messages.push({ role: 'assistant', content: data.content || '暂无回答，请稍后再试。' })
      }).catch(() => {
        this.messages.push({ role: 'assistant', content: '服务暂时不可用，请稍后再试。' })
      }).finally(() => {
        this.loading = false
        this.scrollBottom()
      })
    },
    clearMessages() {
      this.messages = [{
        role: 'assistant',
        content: '对话已清空。请告诉我您想了解的养老服务问题。'
      }]
      this.currentMode = 'local'
    },
    scrollBottom() {
      this.$nextTick(() => {
        const list = this.$refs.messageList
        if (list) list.scrollTop = list.scrollHeight
      })
    }
  }
}
</script>

<style scoped lang="scss">
.ai-page {
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  gap: 16px;
  height: calc(100vh - 50px);
  padding: 16px;
  background: #eef1f5;
}
.ai-sidebar,
.chat-panel {
  background: #fff;
  border: 1px solid #e4e7ed;
}
.ai-sidebar {
  padding: 20px 16px;
}
.assistant-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 20px;
  margin-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
  h2 {
    margin: 0 0 5px;
    font-size: 18px;
    color: #303133;
  }
  p {
    margin: 0;
    font-size: 12px;
    color: #909399;
  }
}
.assistant-avatar,
.message-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: none;
  color: #fff;
  background: #1677ff;
}
.assistant-avatar {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  font-size: 22px;
}
.suggestion {
  display: flex;
  align-items: center;
  gap: 9px;
  width: 100%;
  min-height: 42px;
  padding: 9px 12px;
  margin-bottom: 8px;
  color: #4b5563;
  text-align: left;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
  &:hover {
    color: #1677ff;
    border-color: #a0cfff;
    background: #f2f8ff;
  }
}
.ability-list {
  margin-top: 24px;
  padding: 14px;
  background: #f7f8fa;
  h3 {
    margin: 0 0 12px;
    font-size: 14px;
  }
  p {
    margin: 9px 0;
    color: #606266;
    font-size: 13px;
  }
}
.chat-panel {
  display: grid;
  grid-template-rows: 58px minmax(0, 1fr) 145px;
  min-width: 0;
}
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid #ebeef5;
  strong {
    margin-right: 12px;
    font-size: 16px;
  }
}
.mode-badge {
  padding: 3px 8px;
  color: #168a58;
  font-size: 12px;
  border: 1px solid #b7ebd2;
  background: #f0fff8;
  &.online {
    color: #1677ff;
    border-color: #b3d8ff;
    background: #f0f7ff;
  }
  &.fallback {
    color: #b7791f;
    border-color: #f3d19e;
    background: #fff8eb;
  }
}
.api-name {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
.message-list {
  overflow-y: auto;
  padding: 24px;
  background: #fafbfc;
}
.message-row {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  &.user {
    flex-direction: row-reverse;
    .message-content {
      align-items: flex-end;
    }
    .message-avatar {
      background: #36a269;
    }
    .message-bubble {
      color: #fff;
      background: #1677ff;
    }
  }
}
.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}
.message-content {
  display: flex;
  flex-direction: column;
  max-width: 72%;
}
.message-name {
  margin-bottom: 6px;
  color: #909399;
  font-size: 12px;
}
.message-bubble {
  padding: 12px 15px;
  line-height: 1.75;
  color: #303133;
  border-radius: 6px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
.typing {
  display: flex;
  gap: 5px;
  span {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #909399;
    animation: typing 1.1s infinite ease-in-out;
    &:nth-child(2) { animation-delay: 0.15s; }
    &:nth-child(3) { animation-delay: 0.3s; }
  }
}
.chat-input-area {
  padding: 14px 18px;
  border-top: 1px solid #ebeef5;
  background: #fff;
}
.input-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 9px;
  span {
    color: #909399;
    font-size: 12px;
  }
}
@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.5; }
  30% { transform: translateY(-4px); opacity: 1; }
}
@media (max-width: 900px) {
  .ai-page {
    grid-template-columns: 1fr;
    height: auto;
  }
  .ai-sidebar {
    display: none;
  }
  .chat-panel {
    height: calc(100vh - 82px);
  }
}
</style>
