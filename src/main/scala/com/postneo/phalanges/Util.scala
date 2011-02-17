package com.postneo
package phalanges

object Util {

    val CRLF = "\r\n"
    val TAB = "\t"
    
    def pad(str: String, length: Int = 20) = {
        String.format("%-" + length + "s", str)
    }
    

}
