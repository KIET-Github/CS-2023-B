package com.example.finalprojectv1.utils

class ChatTripDetail {

    var destination : String ?= null
    var source : String ?= null
    var date : String ?= null
    var time : String ?= null
    var str : String ?=null


    constructor(){}

    constructor(destination : String?, source : String?, date : String?, time : String?){
        this.destination = destination
        this.date = date
        this.source = source
        this.time = time
    }

    public fun getstr(): String?{
        return str
    }

    public fun setstr(value : String){
        str = value
    }

}