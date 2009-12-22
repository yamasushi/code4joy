// Sine Strange Attractor

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSSine]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ){
			add( new DSSine( name , 
				(a,b) , numRing,ovalR ) ) 
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		fillParam("",
			ParamRange.neighbor( 1.8  , 0.01 , 20) ,
			ParamRange.neighbor( 0.3  , 0.01 , 20) ,
			5)
	}
}                       
