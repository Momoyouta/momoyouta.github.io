export const ROUTE_PATH = {

    //admin
    ADMIN_ANI: 'anime',
    ADMIN_ANI_ADD: 'add',
    ADMIN_ANI_UPDATE: 'update',
    ADMIN_WEEKLIST: 'weeklistmg',
    ADMIN_ANNOUNCEMNT: 'announcement',
    ADMIN_USERMANAGE: 'usermanager',

    //USER
    USER_PROFILE: 'profile',
    USER_FAVORITE: 'favorites',
    USER_UPDATE_INFO: 'updateInfo',
}

export const AUTH_PATH=[
    `/user/${ROUTE_PATH.USER_FAVORITE}`,
    `/user/${ROUTE_PATH.USER_UPDATE_INFO}`,
]