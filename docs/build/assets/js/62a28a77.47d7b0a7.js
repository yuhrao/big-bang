"use strict";(self.webpackChunkbig_bang=self.webpackChunkbig_bang||[]).push([[9856],{3092:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>c,contentTitle:()=>s,default:()=>d,frontMatter:()=>t,metadata:()=>a,toc:()=>l});var o=i(7624),r=i(2172);const t={sidebar_position:0,custom_edit_url:"https://github.com/yuhrao/big-bang/tree/main/README.md"},s="Config",a={id:"Components/config/index",title:"Config",description:"Read configuration from various sources",source:"@site/docs/Components/config/index.md",sourceDirName:"Components/config",slug:"/Components/config/",permalink:"/big-bang/Components/config/",draft:!1,unlisted:!1,editUrl:"https://github.com/yuhrao/big-bang/tree/main/README.md",tags:[],version:"current",sidebarPosition:0,frontMatter:{sidebar_position:0,custom_edit_url:"https://github.com/yuhrao/big-bang/tree/main/README.md"},sidebar:"tutorialSidebar",previous:{title:"Polylith",permalink:"/big-bang/"},next:{title:"core",permalink:"/big-bang/Components/config/api/yuhrao/config/core/"}},c={},l=[{value:"Libs",id:"libs",level:2},{value:"Usage",id:"usage",level:2},{value:"Config sources",id:"config-sources",level:3},{value:"Getting configuration",id:"getting-configuration",level:3}];function u(e){const n={a:"a",code:"code",h1:"h1",h2:"h2",h3:"h3",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",ul:"ul",...(0,r.M)(),...e.components};return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(n.h1,{id:"config",children:"Config"}),"\n",(0,o.jsx)(n.p,{children:"Read configuration from various sources"}),"\n",(0,o.jsx)(n.p,{children:(0,o.jsx)(n.strong,{children:"Table of Contents"})}),"\n",(0,o.jsxs)(n.ul,{children:["\n",(0,o.jsxs)(n.li,{children:[(0,o.jsx)(n.a,{href:"#config",children:"Config"}),"\n",(0,o.jsxs)(n.ul,{children:["\n",(0,o.jsx)(n.li,{children:(0,o.jsx)(n.a,{href:"#libs",children:"Libs"})}),"\n",(0,o.jsxs)(n.li,{children:[(0,o.jsx)(n.a,{href:"#usage",children:"Usage"}),"\n",(0,o.jsxs)(n.ul,{children:["\n",(0,o.jsx)(n.li,{children:(0,o.jsx)(n.a,{href:"#config-sources",children:"Config sources"})}),"\n",(0,o.jsx)(n.li,{children:(0,o.jsx)(n.a,{href:"#getting-configuration",children:"Getting configuration"})}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,o.jsx)(n.h2,{id:"libs",children:"Libs"}),"\n",(0,o.jsx)(n.p,{children:"None."}),"\n",(0,o.jsx)(n.h2,{id:"usage",children:"Usage"}),"\n",(0,o.jsx)(n.h3,{id:"config-sources",children:"Config sources"}),"\n",(0,o.jsx)(n.p,{children:"Order by priority"}),"\n",(0,o.jsxs)(n.ol,{children:["\n",(0,o.jsx)(n.li,{children:"Java properties"}),"\n",(0,o.jsx)(n.li,{children:"Environment Variables"}),"\n"]}),"\n",(0,o.jsx)(n.h3,{id:"getting-configuration",children:"Getting configuration"}),"\n",(0,o.jsx)(n.p,{children:"You should provide map to specify how to get each value.\nThe result will be a map with the same keys as your specification, but\ninstead the options, will contain respective config values"}),"\n",(0,o.jsx)(n.pre,{children:(0,o.jsx)(n.code,{className:"language-clojure",children:'(require \'[br.dev.yuhri.config.core :as cfg])\n(def config\n  (cfg/create {:port  {:env      "SERVER_PORT" ;; Get value from env SERVER_PORT\n                       :parse-fn #(Integer/parseInt %)} ;; parses value to an integer\n               :host  {:prop "app.server.port"} ;; Get value from property app.server.port\n               :test1 {:env  "TEST_1"\n                       :prop "random.test.1"} ;;  Get value from property app.server.port\n               :abc   {:env      "ABC"\n                       :parse-fn keyword\n                       :default  :def}})) ;; If value didn\'t exists or is empty, uses default value\n\n;; Example output\n;;=>{:port 8080 :host "0.0.0.0" :test "random" :abc :def}\n'})})]})}function d(e={}){const{wrapper:n}={...(0,r.M)(),...e.components};return n?(0,o.jsx)(n,{...e,children:(0,o.jsx)(u,{...e})}):u(e)}},2172:(e,n,i)=>{i.d(n,{I:()=>a,M:()=>s});var o=i(1504);const r={},t=o.createContext(r);function s(e){const n=o.useContext(t);return o.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function a(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:s(e.components),o.createElement(t.Provider,{value:n},e.children)}}}]);