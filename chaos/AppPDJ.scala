// Peter de Jong Strange Attractor

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSPeterDeJong]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				cRange  : Stream[Double] ,
				dRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange){
			add( new DSPeterDeJong( name , 
				(a,b,c,d) ,  numRing,ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		// fillParam("",
			// ParamRange.neighbor( 0.56  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.53  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.274 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.67  , 0.01 ,0) ,
			// 1)
		
		// fillParam("",
			// ParamRange.neighbor( 0.61  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.74  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.498 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.291 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.4035 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.4586 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.393  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.475  , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.53  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.506 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.402 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.434 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.368 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.394 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.362 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.804 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.854 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.252 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.22  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.396 , 0.01 ,0) ,
			// 1)
			
		fillParam("",
			ParamRange.neighbor( 0.854 , 0.01 ,0) ,
			ParamRange.neighbor( 0.404 , 0.01 ,0) ,
			ParamRange.neighbor( 0.742 , 0.01 ,0) ,
			ParamRange.neighbor( 0.51  , 0.01 ,0) ,
			1)
	}
}                       
