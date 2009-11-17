// Simplified Gumowski-Mira Strange Attractor
// 17:42 2009/10/14
// 
// see:
//   http://www.ny.airnet.ne.jp/satoh/chaos.htm
// see also:
//   http://www.scipress.org/journals/forma/pdf/1502/15020121.pdf
//   http://math.cmaisonneuve.qc.ca/alevesque/chaos_fract/Attracteurs/Attracteurs.html

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}

import Math.{sin,cos,log,sqrt,abs,exp}

import DSSimplifiedGumowskiMira._
class DSSimplifiedGumowskiMira(
				val header       :String , 
				//
				val pi           :(Double)=>Double ,
				val psi          :(Double)=>Double ,
				val param        :(Double,Double,Double,Double)    ,
				val map          :((Double,Double))=>(Double,Double) ,
				ovalR:Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (paramA,paramB,paramC,paramD) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR).points
	override val chaosName     = "sgmchaos_" + header + "_" + 
						"("+paramA.formatted("%7.5f")+","+
							paramB.formatted("%7.5f")+","+
							paramC.formatted("%7.5f")+","+
							paramD.formatted("%7.5f")+")"
	
	override val chaosSystem = new ChaosSystem{
		val (a,b,c,d) = param
		//
		override def mapCoordinate( p:(Double,Double) ) : (Double,Double) = { map(p) }
		//
		override def mapDifference( p:(Double,Double) ) : (Double,Double) = {
			val (x,y) = p
			//
			val xx = a*x + b*y + (c*pi(x)+d)/(1+psi(x))
			val yy = -x
			//
			(xx,yy)
		}
		
		override def validateParam : Boolean = {
			// coefficient of pi
			if( abs(c) < 1e-7 ) return false
			
			true 
		}
	}
}

object DSSimplifiedGumowskiMira extends ChaosParameter[DSSimplifiedGumowskiMira]
{
	def fillParam(name:String ,
				pi     : (Double)=>Double       ,
				psi    : (Double)=>Double       ,
				aRange : Stream[Double] ,
				bRange : Stream[Double] ,
				cRange : Stream[Double] ,
				dRange : Stream[Double] ,
				ovalR  : Double )(map:((Double,Double,Double,Double))=>((Double,Double))=>(Double,Double) ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange) {
			val par = (a,b,c,d)
			add( new DSSimplifiedGumowskiMira( name ,
						pi , psi , par , map(par) , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		def mapA(param:(Double,Double,Double,Double)) : ((Double,Double))=>(Double,Double) = {
			val (a,b,c,d) = param
			val ratio = abs(a)+1 //4*abs(phi(1.0)) // experimental scale ratio for width
			
			import Transform._
			val theta = (3*Math.Pi)/4.0
			//
			if (a>= 0) { p => scale(ratio,    1)(rotate(theta)(p) ) }
			else       { p => scale(1    ,ratio)(rotate(theta)(p) ) }
		}
		
		var maps  =new Queue[(String,(Double)=>Double,(Double)=>Double)]
		var params=new Queue[(Stream[Double],Stream[Double],Stream[Double],Stream[Double],Double)]
		
		// maps=maps enqueue("_"       ,{x=>x*x         },{x=>x*x        })
		// maps=maps enqueue("A"       ,{x=>abs(x)      },{x=>abs(x)     })
		// maps=maps enqueue("Q"       ,{x=>x*x*x*x     },{x=>x*x*x*x    })
		// maps=maps enqueue("C"       ,{x=>abs(x*x*x)  },{x=>abs(x*x*x) })
		// maps=maps enqueue("A10"     ,{x=>x+1         },{x=>abs(x)     })
		// maps=maps enqueue("AA10"    ,{x=>abs(x+1)    },{x=>abs(x)     })
		// maps=maps enqueue("WA"      ,{x=>abs(x)      },psiW )
		// maps=maps enqueue("W0"      ,{x=> 1          },psiW )
		// maps=maps enqueue("WR"      ,{x=>sqrt(abs(x))},psiW )
		// maps=maps enqueue("WW"      ,psiW             ,psiW )
		// maps=maps enqueue("GB"      ,{t=>abs(t)} , {t=>0} )
		// maps=maps enqueue("_210_124",{x => x*x -2*x +4 } , {x => x*x } )
		
		maps=maps enqueue("AP1AP2_R"  , {x =>       sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) 
		maps=maps enqueue("AP1AP2_1R" , {x =>x     *sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) 
		maps=maps enqueue("AP1AP2_AR" , {x =>abs(x)*sqrt(abs(x)) } , {x =>abs(x+1)*abs(x+2) } ) 
		
		// maps=maps enqueue("AP1AP2_0" , {x => 1 } , {x =>abs(x+1)*abs(x+2) } ) // favorite
		// maps=maps enqueue("_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => x*x } ) // favorite
		
		// maps=maps enqueue("_11_AM1AM2",{x =>abs(x-1)*abs(x-2) } , {x => x*x } )
		// maps=maps enqueue("_11_AP1AP2",{x =>abs(x+1)*abs(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_AM1AP2",{x =>abs(x-1)*abs(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => x*x } )
		
		// maps=maps enqueue("AM1AM2_0" , {x => 1 } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_0" , {x => 1 } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_0" , {x => 1 } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_0" , {x => 1 } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("AM1AM2_1" , {x => x } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_1" , {x => x } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_1" , {x => x } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_1" , {x => x } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("AM1AM2_2" , {x => x*x } , {x =>abs(x-1)*abs(x-2) } )
		// maps=maps enqueue("AP1AP2_2" , {x => x*x } , {x =>abs(x+1)*abs(x+2) } )
		// maps=maps enqueue("AM1AP2_2" , {x => x*x } , {x =>abs(x-1)*abs(x+2) } )
		// maps=maps enqueue("AP1AM2_2" , {x => x*x } , {x =>abs(x+1)*abs(x-2) } )
		
		// maps=maps enqueue("A111_11_AM1AM2",{x =>abs(x-1)*abs(x-2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AP1AP2",{x =>abs(x+1)*abs(x+2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AM1AP2",{x =>abs(x-1)*abs(x+2) } , {x => abs(x*x+x+1) } )
		// maps=maps enqueue("A111_11_AP1AM2",{x =>abs(x+1)*abs(x-2) } , {x => abs(x*x+x+1) } )
		
		// maps=maps enqueue("_11_M1M2",{x =>(x-1)*(x-2) } , {x => x*x } )
		// maps=maps enqueue("_11_P1P2",{x =>(x+1)*(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_M1P2",{x =>(x-1)*(x+2) } , {x => x*x } )
		// maps=maps enqueue("_11_P1M2",{x =>(x+1)*(x-2) } , {x => x*x } )
		
		// maps=maps enqueue("A11_M1M2",{x =>(x-1)*(x-2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_P1P2",{x =>(x+1)*(x+2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_M1P2",{x =>(x-1)*(x+2) } , {x => abs(x) } )
		// maps=maps enqueue("A11_P1M2",{x =>(x+1)*(x-2) } , {x => abs(x) } )
		
		params=params enqueue (
					ParamRange.neighbor(-0.4 , 0.01 , 6) , // type of shape
					ParamRange.neighbor( 1   , 0.1 ,0) , 
					ParamRange.neighbor( 1   , 0.1 ,0) , 
					ParamRange.neighbor( 0   , 0.1 ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor( 0.3 , 0.1 , 6) , // type of shape
					ParamRange.neighbor( 1   , 0.1 ,0) , 
					ParamRange.neighbor( 1   , 0.1 ,0) , 
					ParamRange.neighbor( 0   , 0.1 ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor(-1.79160, 0.0001  ,0) , // type of shape
					ParamRange.neighbor( 0.9999 , 0.0001  ,0) , 
					ParamRange.neighbor( 1.07   , 0.01    ,0) , 
					ParamRange.neighbor( 0      , 0.1     ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor( 0  , 0.0001  ,0) , // type of shape
					ParamRange.neighbor( 1  , 0.0001  ,0) , 
					ParamRange.neighbor(-1  , 0.0001  ,0) , 
					ParamRange.neighbor(-1  , 0.0001  ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor(-1.541 , 0.001 ,0) , // type of shape
					ParamRange.neighbor( 1.0   , 0.001 ,0) , 
					ParamRange.neighbor( 1.0   , 0.01  ,0) , 
					ParamRange.neighbor( 0.0   , 0.1   ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor(-1.541 , 0.001 ,0) , // type of shape
					ParamRange.neighbor( 1.0   , 0.001 ,0) , 
					ParamRange.neighbor( 1.07  , 0.01  ,0) , 
					ParamRange.neighbor(-0.1   , 0.1   ,0) , 
					1.0 )
					
		params=params enqueue (
					ParamRange.neighbor(-1.6   , 0.01  ,0) , // type of shape
					ParamRange.neighbor( 1.0   , 0.001 ,0) , 
					ParamRange.neighbor( 1.07  , 0.01  ,0) , 
					ParamRange.neighbor( 0     , 0.1   ,0) , 
					1.0 )
		
		params=params enqueue (
					ParamRange.neighbor( 0 , 0.001 , 0) , // type of shape
					ParamRange.neighbor( 1 , 0.001 , 0) , 
					ParamRange.neighbor( 1 , 0.001 , 0) , 
					ParamRange.neighbor( 0 , 0.001 , 0) , 
					1.0 ) // favorite
		
		params=params enqueue (
					ParamRange.neighbor( 0.1 , 0.001 , 0) , // type of shape
					ParamRange.neighbor( 1   , 0.001 , 0) , 
					ParamRange.neighbor( 1   , 0.001 , 0) , 
					ParamRange.neighbor( 0   , 0.001 , 0) , 
					1.0 ) // favorite
		
		params=params enqueue (
					ParamRange.neighbor(-0.9  , 0.01 , 0) , // type of shape
					ParamRange.neighbor( 0.96 , 0.01 , 0) , 
					ParamRange.neighbor(-4.0  , 0.01 , 0) , 
					ParamRange.neighbor( 0.0  , 0.01 , 0) , 
					1.0 ) // favorite
		
		// for(m<-maps;p<-params) {
			// fillParam( "RAW-"+m._1 , m._2,m._3 , p._1,p._2,p._3,p._4,p._5){ _ =>{p=>p}}
		// }
		
		for(m<-maps;p<-params) {
			fillParam( m._1 , m._2,m._3 , p._1,p._2,p._3,p._4,p._5)(mapA)
		}
	}
}

