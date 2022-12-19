package com.example.finalprojectv1.utils

class Message {

    var message : String ?= null
    var userId : String ?= null

    constructor()

    constructor(message: String, userId: String){
        this.message = message
        this.userId = userId
    }

}