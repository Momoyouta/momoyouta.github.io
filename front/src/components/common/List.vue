<template>
  <div class="list-container">
    <div
      v-for="(row, rowIndex) in rows"
      :key="rowIndex"
      class="list-row"
      :style="getRowStyle(row)"
    >
      <div
        v-for="(item, colIndex) in row"
        :key="colIndex"
        class="list-item"
        :style="getItemStyle(colIndex, row)"
      >
        <slot name="default" :item="item" :index="rowIndex * n + colIndex">{{ item }}</slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { toRefs } from 'vue'

const props = defineProps({
  items: {
    type: Array,
    required: true
  },
  n: {
    type: Number,
    default: 6
  },
  itemWidth: {
    type: Number,
    default: 15
  }
})

const { items, n ,itemWidth} = toRefs(props)

const rows = computed(() => {
  const result = []
  for (let i = 0; i < items.value.length; i += n.value) {
    result.push(items.value.slice(i, i + n.value))
  }
  return result
})
const gap = computed(() => {
  if (n.value <= 1) return '0px'
  return `calc((100% - ${n.value} * ${itemWidth.value}%) / ${n.value - 1})`
})

function getRowStyle(row) {
  if (row.length === n.value) {
    return { justifyContent: 'space-between' }
  } else {
    return { justifyContent: 'flex-start' }
  }
}

function getItemStyle(colIndex, row) {
  // 最后一列不加margin
  if (colIndex !== row.length - 1) {
    return { width: itemWidth.value + '%', 'margin-right': gap.value }
  } else {
    return { width: itemWidth.value + '%' }
  }
}
</script>

<style scoped>
.list-container {
  width: 100%;
}
.list-row {
  display: flex;
  margin-bottom: 16px;
}
.list-item {
  /* 宽度和间隔由style绑定 */
}
</style>

