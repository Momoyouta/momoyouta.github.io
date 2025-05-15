export function hasRole(...roles) {
    const userRoles=localStorage.getItem("roles");
    if(userRoles==null){
        return false;
    }
    const userRoleP=JSON.parse(userRoles);
    return userRoleP.every((role)=>roles.includes(role));
}

export function hasAnyRole(...roles) {
    const userRoles=localStorage.getItem("roles");
    if(userRoles==null){
        return false;
    }
    const userRoleP=JSON.parse(userRoles);
    return userRoleP.some((role)=>roles.includes(role));
}