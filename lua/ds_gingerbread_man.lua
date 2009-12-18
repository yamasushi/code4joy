require "vector"

DSGingerbreadMan = {}
DSGingerbreadMan_mt = {__index = DSGingerbreadMan}

function DSGingerbreadMan:new()
	local o = {}
	o.ds = function(pt)
		local x,y = pt:xy()
		local xx = 1-y+math.abs(x)
		local yy = x
		return Vector:new(xx,yy)
	end
	return setmetatable(o,DSGingerbreadMan_mt)
end

DSGingerbreadMan_mt.__tostring = function(gm)
	return "gbmchaos"
end
