require "vector"

DSGumowskiMira = {}
DSGumowskiMira_mt = {__index = DSGumowskiMira}

function DSGumowskiMira:new(
					param_header ,
					param_a  ,
					param_b  ,
					param_mu ,
					param_nu ,
					param_pi  ,
					param_psi ,
					param_meta_phi )
	assert(param_header)
	assert(param_a)
	assert(param_b)
	assert(param_mu)
	assert(param_nu)
	assert(param_pi)
	assert(param_psi)
	assert(param_meta_phi)

	local o = {}
	o.header=param_header
	o.a  = param_a
	o.b  = param_b
	o.mu = param_mu
	o.nu = param_nu
	o.pi = param_pi
	o.psi=param_psi
	o.meta_phi = param_meta_phi
	o.phi = o.meta_phi( o.pi , o.psi )

	local gm_f = function(x)
		return o.mu * x + 2*(1-o.mu)*o.phi(x)
	end

	local gm_g = function(y)
		return o.nu *y + o.a * (1-o.b*y*y)*y
	end

	o.ds = function(pt)
		local x,y = pt:xy()
		local xx = gm_g(y) + gm_f(x)
		local yy = -x + gm_f(xx)
		return Vector:new(xx,yy)
	end

	return setmetatable(o,DSGumowskiMira_mt)
end

DSGumowskiMira_mt.__tostring = function(gm)
	return string.format("gmchaos_%s_(%7.5f,%7.5f)(%7.5f,%7.5f)",
						gm.header , gm.a , gm.b , gm.mu , gm.nu )
end
