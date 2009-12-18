require "vector"
require "frame"

ChaosSystem = {}
ChaosSystem_mt = {__index = ChaosSystem}

function ChaosSystem:new(sys,param_max_iter)
	assert(sys)
	assert(param_max_iter)
	local o = {}
	o.sys = sys
	o.max_iter = param_max_iter
	return setmetatable(o,ChaosSystem_mt)
end

ChaosSystem_mt.__tostring = function(a)
	return string.format("(sys=%s,max_iter=%d)",
						tostring(a.sys),a.max_iter )
end

-- op Vector -->
function ChaosSystem:iterate(pt0,op)
	assert(self)
	assert(self.max_iter)
	assert(self.sys)
	assert(self.sys.ds)
	local pt = pt0
	for i= 1 , self.max_iter do
		op(pt)
		pt = self.sys.ds(pt)
	end
end

-- tf Vector ---> Vector
-- op Vector --->
function ChaosSystem:iterate_with(pt0,tf,op)
	assert(self)
	assert(pt0)
	assert(tf)
	assert(op)
	self:iterate(pt0 ,
		function(pt)
			op( tf(pt) )
		end )
end

function ChaosSystem:calc_frame(pt0,data_frame)
	assert(self)
	assert(self.max_iter)
	local frame = (data_frame) and data_frame or Frame.empty
	self:iterate(pt0,function(pt) frame = frame:inflate(pt) end)
	return frame
end



