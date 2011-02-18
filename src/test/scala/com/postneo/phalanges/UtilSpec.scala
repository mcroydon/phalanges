package com.postneo
package phalanges

import org.specs.Specification

object UtilSpec extends Specification {
    "Util" should {
        
        "pad strings to 20 characters by default" in {
            Util.pad("hello").size mustEqual 20
        }
        
        "pad strings to a specified width if specified" in {
            Util.pad("world", 42).size mustEqual 42
        }
        
        "contain TAB and CRLF constants" in {
            Util.CRLF mustEqual "\r\n"
            Util.TAB mustEqual "\t"
        }
    }
}
