package com.postneo
package phalanges

import net.lag.configgy.Configgy

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer

class User(uname: String, name: String, plan: String) {
    val username = uname
    
    def to_index_string() = {
        Util.pad(username) + Util.TAB + Util.pad(name) + Util.CRLF
    }

    def to_detail_string() = {
        to_index_string() + "Plan:" + Util.CRLF +  plan + Util.CRLF
    }

}
