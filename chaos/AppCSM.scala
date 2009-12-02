// Chirikov Standard map Strange Attractor
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSChirikov]
{
	def fillParam(name:String   ,
				kRange : Stream[Double] ,
				map    : (Vector[Double])=>Vector[Double] , 
				ovalR  : Double ) : Unit = {
		for ( k<-kRange ){
			add( new DSChirikov( name , k , map, numRing,ovalR )  )
		}
		//
		()
	}
	
	//----------------------------
	def setup() : Unit = {
		def mapD1(r:Double)(xp:Vector[Double]):Vector[Double] = {
			val (x,p) = ( xp.x , xp.y )
			val theta = x
			val radius= p+r
			( radius*cos(theta) , radius*sin(theta) )
		}
		def mapD2(r:Double)(xp:Vector[Double]):Vector[Double] = {
			val (x,p) = ( xp.x , xp.y )
			val theta = p
			val radius= x+r
			( radius*cos(theta) , radius*sin(theta) )
		}
		
		var params = new Queue[(String,Stream[Double])]
		var maps   = new Queue[(String,(Vector[Double])=>Vector[Double])]
		
		params = params enqueue ("H" ,ParamRange.neighbor( 1. / 34.0     , 0.01 , 1 )) //Hermann 
		params = params enqueue ("CC",ParamRange.neighbor( 419. / 500.0  , 0.01 , 1 )) //Cellette,Chierchia 1995 
		params = params enqueue ("G" ,ParamRange.neighbor( 0.971635406   , 0.01 , 1 )) //Greene 
		params = params enqueue ("MP",ParamRange.neighbor( 63.0 / 64.0   , 0.01 , 1 )) //MacKay , Percival 1995
		params = params enqueue ("MA",ParamRange.neighbor( 4.0 / 3.0     , 0.01 , 1 )) //Mather 1995
		params = params enqueue ("_" ,ParamRange.neighbor( 2             , 0.01 , 1 )) //
		
		maps = maps enqueue (""  ,{ p => p })
		maps = maps enqueue ("D1",mapD1(2*Math.Pi) _ )
		maps = maps enqueue ("D2",mapD2(2*Math.Pi) _ )
		
		for( hp<-params ; m<-maps) {
			fillParam(hp._1+"-"+m._1  , hp._2 , m._2 , 1 )
		}
	}
}

