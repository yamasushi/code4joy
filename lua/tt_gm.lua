require "vector"
require "geometry"
require "chaos_renderer"
require "chaos_system"

require "ds_gumowski_mira"

local img_width  = 3000
local img_height = 3000
local oval_r     = 0.1

local max_iter= 2000
local num_traj= 100

local header = {"_","A","Q","C"}
local common_fn = {}
common_fn["_"] = function(t) return t*t end
common_fn["A"] = math.abs
common_fn["Q"] = function(t) return t*t*t*t end
common_fn["C"] = function(t) return math.abs(t*t*t) end


local pi={}
local psi={}
for k,v in pairs(common_fn) do
	pi[k] = v
	psi[k] = v
end

local meta_phi = function(param_pi,param_psi)
	return function(t) return param_pi(t)/(1+param_psi(t)) end
end

local params = {}
for i,h in ipairs(header) do
	--print(i,h,pi[h],psi[h])
	table.insert(params,DSGumowskiMira:new(h,0.008,0.05,-0.485,1.0 , pi[h] , psi[h] , meta_phi))
	table.insert(params,DSGumowskiMira:new(h,0.008,0.05, 0    ,1.0 , pi[h] , psi[h] , meta_phi))
end

for i,gm in ipairs(params) do
	print(i,gm)
	chaos_system = ChaosSystem:new(gm,max_iter)
	--print(chaos_system)
	cr = ChaosRenderer:new(chaos_system,num_traj,oval_r)
	--print(cr)
	cr:render(img_width,img_height)
end
