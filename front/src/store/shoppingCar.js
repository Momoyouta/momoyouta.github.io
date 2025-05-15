
export const useCartStore = ('shoppingCartStore',{
    state:()=>({
        count:0,
    }),
    getters:{

    },
    actions:{
        countAdd(){
            this.count++;
        },
    }
});