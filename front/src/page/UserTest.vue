<template>
  <div class="container">
    <!-- 列表A -->
    <ul
        class="list"
        :class="{ active: currentListId === 'A' }"
        data-list-id="A"
    >
      <li
          v-for="item in listA"
          :key="item.id"
          class="item"
          :data-id="item.id"
          @mousedown.prevent="startDrag($event, item, 'A')"
      >
        {{ item.text }}
      </li>
    </ul>

    <!-- 列表B -->
    <ul
        class="list"
        :class="{ active: currentListId === 'B' }"
        data-list-id="B"
    >
      <li
          v-for="item in listB"
          :key="item.id"
          class="item"
          :data-id="item.id"
          @mousedown.prevent="startDrag($event, item, 'B')"
      >
        {{ item.text }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const listA = ref([
  { id: 1, text: 'Item A-1' },
  { id: 2, text: 'Item A-2' },
  { id: 3, text: 'Item A-3' },
]);

const listB = ref([
  { id: 4, text: 'Item B-1' },
  { id: 5, text: 'Item B-2' },
]);

const draggedItem = ref(null);
const sourceListId = ref('');
const currentListId = ref('');
const cloneElement = ref(null);
const initialPos = ref({ x: 0, y: 0 });

function startDrag(e, item, listId) {
  draggedItem.value = item;
  sourceListId.value = listId;

  const targetEl = e.target.closest('.item');
  targetEl.classList.add('dragging');

  // 创建克隆元素
  createCloneElement(targetEl, e.clientX, e.clientY);

  document.addEventListener('mousemove', handleDrag);
  document.addEventListener('mouseup', endDrag);
}

function createCloneElement(sourceElement, clientX, clientY) {
  // 克隆原始元素
  cloneElement.value = sourceElement.cloneNode(true);
  const rect = sourceElement.getBoundingClientRect();

  // 设置克隆元素样式
  cloneElement.value.style.position = 'fixed';
  cloneElement.value.style.width = `${rect.width}px`;
  cloneElement.value.style.height = `${rect.height}px`;
  cloneElement.value.style.opacity = '0.8';
  cloneElement.value.style.pointerEvents = 'none';
  cloneElement.value.style.transform = 'translate(-50%, -50%)';
  cloneElement.value.style.zIndex = '9999';
  cloneElement.value.style.left = `${clientX}px`;
  cloneElement.value.style.top = `${clientY}px`;

  // 保存初始位置
  initialPos.value = {
    x: clientX - rect.left,
    y: clientY - rect.top
  };

  document.body.appendChild(cloneElement.value);
}

function handleDrag(e) {
  if (!cloneElement.value) return;

  // 更新克隆元素位置
  cloneElement.value.style.left = `${e.clientX}px`;
  cloneElement.value.style.top = `${e.clientY}px`;

  // 查找当前悬停的列表
  const hoveredEl = document.elementFromPoint(e.clientX, e.clientY);
  const hoveredList = hoveredEl?.closest('.list');
  currentListId.value = hoveredList?.dataset.listId || '';

  // 更新元素位置
  if (hoveredList) {
    updateElementPosition(e.clientY, hoveredList);
  }
}

function updateElementPosition(y, listEl) {
  const items = [...listEl.children].filter(
      child => !child.classList.contains('dragging')
  );

  // 查找插入位置
  let insertIndex = items.findIndex(item => {
    const rect = item.getBoundingClientRect();
    return y < rect.top + rect.height / 2;
  });

  // 临时调整元素位置
  if (insertIndex === -1) insertIndex = items.length;

  // 添加交换动画
  if (sourceListId.value === currentListId.value) {
    const currentIndex = items.findIndex(
        item => item.dataset.id === draggedItem.value.id.toString()
    );
    if (currentIndex !== -1 && currentIndex !== insertIndex) {
      addSwapAnimation(items[currentIndex], items[insertIndex]);
    }
  }
}

function addSwapAnimation(item1, item2) {
  item1.style.transform = 'translateY(10px)';
  item2.style.transform = 'translateY(-10px)';
  setTimeout(() => {
    item1.style.transform = '';
    item2.style.transform = '';
  }, 300);
}

function endDrag() {
  if (!draggedItem.value || !currentListId.value) {
    cleanup();
    return;
  }

  const targetListId = currentListId.value;
  const sourceList = sourceListId.value === 'A' ? listA : listB;
  const targetList = targetListId === 'A' ? listA : listB;

  // 获取最终插入位置
  const listEl = document.querySelector(`[data-list-id="${targetListId}"]`);
  const items = [...listEl.children].filter(
      child => !child.classList.contains('dragging')
  );

  const insertIndex = items.findIndex(item => {
    const rect = item.getBoundingClientRect();
    return cloneElement.value.style.top < rect.top + rect.height / 2;
  });

  // 更新数据
  const itemIndex = sourceList.value.findIndex(
      item => item.id === draggedItem.value.id
  );
  const [movedItem] = sourceList.value.splice(itemIndex, 1);

  if (targetListId === sourceListId.value) {
    const newIndex = insertIndex === -1 ? items.length : insertIndex;
    sourceList.value.splice(newIndex, 0, movedItem);
  } else {
    targetList.value.splice(insertIndex, 0, movedItem);
  }

  cleanup();
}

function cleanup() {
  cloneElement.value?.remove();
  document.querySelectorAll('.item').forEach(el => {
    el.classList.remove('dragging');
    el.style.transform = '';
  });

  draggedItem.value = null;
  sourceListId.value = '';
  currentListId.value = '';
  initialPos.value = { x: 0, y: 0 };

  document.removeEventListener('mousemove', handleDrag);
  document.removeEventListener('mouseup', endDrag);
}
</script>

<style>
.container {
  display: flex;
  gap: 20px;
  padding: 20px;
}

.list {
  width: 300px;
  border: 2px solid #eee;
  padding: 10px;
  border-radius: 8px;
  background: #f9f9f9;
  list-style: none;
  margin: 0;
  min-height: 200px;
}

.list.active {
  border-color: #2196f3;
}

.item {
  padding: 12px;
  margin: 8px 0;
  background: white;
  border: 1px solid #ddd;
  cursor: move;
  transition: all 0.3s ease;
}

.item.dragging {
  opacity: 0.5;
  transform: scale(0.95);
}
</style>