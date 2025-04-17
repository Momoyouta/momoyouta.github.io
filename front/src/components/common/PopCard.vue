<template>
  <div class="container" :style="style">
    <slot></slot>
  </div>
</template>

<script setup>

import {computed, watch} from "vue";

const props = defineProps({
  turn:{
    required: true,
    type: Number
  },
  width:{
    required: false,
    type: Number,
    default: 15,
  },
  height:{
    required: false,
    type: Number,
    default: 30,
  },
  offset:{
    required: false,
    default: 0,
    type: Number,
  },
  pos:{
    required: false,
    type: String,
    default: 'right'
  }
})
const style=computed(()=>{
  const res={
    height:props.height+'rem',
    width:props.width+'rem',
  }
  switch (props.pos){
    case 'right':
      res['transform']=`translateX(${props.turn ? `-${props.width}rem`:0})`
      break;
    case 'left':
      res['transform']=`translateX(${props.turn ? `${props.width}rem`:0})`
      break;
    case 'top':
      res['transform']=`translateY(${props.turn ? `${props.height}rem`:0})`
      break;
    case 'bottom':
      res['transform']=`translateY(${props.turn ? `-${props.height}rem`:0})`
  }
  if (['right', 'left'].includes(props.pos)) {
    res[props.pos] = `-${props.width}rem`;
    res['top']=`${props.offset}rem`;

  } else if (['top', 'bottom'].includes(props.pos)) {
    res[props.pos] = `-${props.height}rem`;
    res['left']=`${props.offset}rem`;
  }
  return res
})

</script>

<style scoped>
.container{
  position: fixed;
  right: 0;
  top:0;
  border-radius: 12px;
  background: #fff;
  z-index: 99999;
  transition: transform ease 0.5s;
  overflow: hidden;
  padding: 5px;
}
</style>