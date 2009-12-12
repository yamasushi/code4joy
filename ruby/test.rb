
coef =	{	# coefficients named A through Y (25 possible value)
					'A'=>-1.2,
					'B'=>-1.1,
					'C'=>-1.0,
					'D'=>-0.9,
					'E'=>-0.8,
					'F'=>-0.7,
					'G'=>-0.6,
					'H'=>-0.5,
					'I'=>-0.4,
					'J'=>-0.3,
					'K'=>-0.2,
					'L'=>-0.1,
					'M'=> 0.0, # <---- Zero
					'N'=>+0.1,
					'O'=>+0.2,
					'P'=>+0.3,
					'Q'=>+0.4,
					'R'=>+0.5,
					'S'=>+0.6,
					'T'=>+0.7,
					'U'=>+0.8,
					'V'=>+0.9,
					'W'=>+1.0,
					'X'=>+1.1,
					'Y'=>+1.2
					#
}
		123456123456
sign = 'FIRCOERRPVLD' # 12charactor sgnature of system 
#"AMTMNQQXUYGA"
#"CVQKGHQTPNTE"

a = sign.scan(/./).map{|x| coef[x] }
p a
