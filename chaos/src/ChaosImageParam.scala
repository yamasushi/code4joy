// 2009-09-28
// 8:15 2009/11/07

import java.awt.{Graphics2D,Color}

trait ChaosImageParam
{
	val colorFGMap :(Int) => Color = ChaosImageParam.goldFGMap
	val chaosName  :String
	//
	def drawPoint(g:Graphics2D)(count:Int,p:(Int,Int)) : Unit = {
		val (ix,iy)   = p
		g.setColor( colorFGMap(count) )
		g.drawLine( ix , iy , ix , iy )
	}
}

object ChaosImageParam
{
	// gold         #FFD700
	// goldenrod    #daa520
	// darkgoldenrod #b8860b
	// palegoldenrod #eee8aa
	// lightgoldenrodyellow #fafad2 
	def goldFGMap(counter:Int) : Color = {
		if( counter < 0 ) {
			// Drop iteration
			new Color(0xFF,0xD7,0x00)
		}
		else {
			Color.YELLOW
		}
	}
	
	// silver       #c9caca
	// silver gray  #afafb0
	// silver white #efefef
	def silverFGMap(counter:Int) : Color = {
		if( counter < 0 ) {
			// Drop iteration
			new Color( 0xC9 , 0xCA , 0xCA)
		}
		else {
			new Color( 0xef , 0xef , 0xef)
		}
	}
	
}
