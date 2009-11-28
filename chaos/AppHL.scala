// Henon/Lozi Strange Attractor
// 9:00 2009/11/03
import scala.collection.immutable._

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSHenonLozi]
{
	//----------------------------
	def fillParam(name:String ,
				phi:(Double,Double)=>Double ,
				alphaRange : Stream[Double] ,
				betaRange  : Stream[Double] ,
				ovalR      : Double ) : Unit = {
		for ( alpha<-alphaRange ; beta<-betaRange ){
			add( new DSHenonLozi( name , 
				phi , (alpha,beta) , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps = new Queue[(String,(Double,Double)=>Double)]
		maps = maps enqueue ( "H"  , {(x:Double,y:Double)=>x*x    }      ) 
		maps = maps enqueue ( "L"  , {(x:Double,y:Double)=>abs(x) }      ) 
		maps = maps enqueue ( "ML" , {(x:Double,y:Double)=>y*y - abs(x) }) 
		//
		var params = new Queue[(Stream[Double],Stream[Double],Double)] 
		params = params.enqueue(	ParamRange.neighbor(-1.8 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3 , 0.001 ,0) , 0.1 )
					
		params = params.enqueue(	ParamRange.neighbor(-1.8 , 0.01  ,0) ,
									ParamRange.neighbor( 0.5 , 0.001 ,0) , 0.1 )
		
		params = params.enqueue(	ParamRange.neighbor( 1.4 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3 , 0.001 ,0) , 0.1 )
		
		params = params.enqueue(	ParamRange.neighbor( 1.35 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3  , 0.001 ,0) , 0.1 )
			
		params = params.enqueue(	ParamRange.neighbor( 0.2  , 0.01  ,0) ,
									ParamRange.neighbor( 1.01 , 0.001 ,0) , 0.1 )
		for( m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3)
		}
	}
}                       
