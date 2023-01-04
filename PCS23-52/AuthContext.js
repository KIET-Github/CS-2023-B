import { createContext, useState } from "react";

export const Auth = createContext()

export const AuthContext = ({children}) => {

    const [curr_user, setCurr_user] = useState({})

    return (
        <Auth.Provider value={{curr_user, setCurr_user}}>
            {children}
        </Auth.Provider>
    )
}


