// Clifford Strange Attractor
chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSClifford]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				cRange  : Stream[Double] ,
				dRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange){
			add( new DSClifford( name , 
				(a,b,c,d) ,  numRing,ovalR )  )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		// fillParam("",
			// ParamRange.neighbor(-1.4  , 0.01 ,0) ,
			// ParamRange.neighbor( 1.6  , 0.01 ,0) ,
			// ParamRange.neighbor( 1    , 0.01 ,0) ,
			// ParamRange.neighbor( 0.7  , 0.01 ,0) ,
			// 1)
		fillParam("",
			ParamRange.neighbor( 1.1  , 0.01 ,0) ,
			ParamRange.neighbor(-1.0  , 0.01 ,0) ,
			ParamRange.neighbor( 1.0  , 0.01 ,0) ,
			ParamRange.neighbor( 1.5  , 0.01 ,0) ,
			1)
	}
}                       
