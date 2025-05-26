export function tranToCard(data,showType){
    if(showType === undefined){
        showType={
            score: false,
            state: true,
            date: true,
            name: true
        }
    }
    let anilist=[];
    for(let item of data){
        let anttp={
            show:showType,
            data: {
                ...item.anime,
                ep:item.ep,
                end:item.ep===item.totalEps?1:0,
            }
        }
        let date=anttp.data.updateTime.split("-");
        anttp.data.updateTime=date[1]+'/'+date[2].split("T")[0]
        anilist.push(anttp);
    }
    return anilist;
}