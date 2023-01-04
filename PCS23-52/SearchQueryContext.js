import { createContext, useState } from "react";

export const SearchQuery = createContext()

export const SearchQueryContext = ({children}) => {

    const [query, setQuery] = useState("")

    return (
        <SearchQuery.Provider value={{query, setQuery}}>
            {children}
        </SearchQuery.Provider>
    )
}


