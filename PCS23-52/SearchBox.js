import React, {useContext, useEffect, useState} from 'react'
import '../styles/searchbox.css'
import dummyData from "../dummyData"
import SingleSearchItem from './SingleSearchItem'
import axios from 'axios'
import { SearchQuery } from '../context/SearchQueryContext'

const SearchBox = () => {

    const {query, setQuery} = useContext(SearchQuery)

    const [data, setData] = useState([])
    useEffect(() => {

        const getData = () => {
            const obj = {
                query: query
            }
            if(query && query.length > 0){
                axios.post("http://localhost:9002/api/getFilteredData", obj)
                .then(res => setData(res.data.res_user))
            }
        }
        getData()
    
    }, [query])

    return (
        <div className='search-box-container'>
            <div className='search-box'>
                {
                    (query && query.length > 0 && data.map(item => {
                        return (
                            <>
                                <SingleSearchItem item={item} key={item._id}/>
                            </>
                        )
                    }))
                }
            </div>
        </div>
    )
}

export default SearchBox