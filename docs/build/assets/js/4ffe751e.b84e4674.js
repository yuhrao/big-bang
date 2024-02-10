"use strict";(self.webpackChunkbig_bang=self.webpackChunkbig_bang||[]).push([[9648],{4460:(e,a,n)=>{n.r(a),n.d(a,{assets:()=>i,contentTitle:()=>r,default:()=>h,frontMatter:()=>o,metadata:()=>c,toc:()=>t});var l=n(7624),s=n(2172);const o={sidebar_label:"string",title:"yuhrao.data-cloak.core.string",toc_min_heading_level:2,toc_max_heading_level:4,custom_edit_url:null},r=void 0,c={id:"Components/data-cloak/api/yuhrao/data-cloak/core/string/index",title:"yuhrao.data-cloak.core.string",description:"all",source:"@site/docs/Components/data-cloak/api/yuhrao/data-cloak/core/string/index.md",sourceDirName:"Components/data-cloak/api/yuhrao/data-cloak/core/string",slug:"/Components/data-cloak/api/yuhrao/data-cloak/core/string/",permalink:"/big-bang/Components/data-cloak/api/yuhrao/data-cloak/core/string/",draft:!1,unlisted:!1,editUrl:null,tags:[],version:"current",frontMatter:{sidebar_label:"string",title:"yuhrao.data-cloak.core.string",toc_min_heading_level:2,toc_max_heading_level:4,custom_edit_url:null},sidebar:"tutorialSidebar",previous:{title:"map",permalink:"/big-bang/Components/data-cloak/api/yuhrao/data-cloak/core/map/"},next:{title:"map",permalink:"/big-bang/Components/data-cloak/api/yuhrao/data-cloak/map/"}},i={},t=[{value:"all",id:"all",level:3},{value:"email",id:"email",level:3},{value:"offset",id:"offset",level:3},{value:"phone",id:"phone",level:3},{value:"symmetric-offset",id:"symmetric-offset",level:3}];function d(e){const a={a:"a",code:"code",h3:"h3",li:"li",p:"p",pre:"pre",ul:"ul",...(0,s.M)(),...e.components};return(0,l.jsxs)(l.Fragment,{children:[(0,l.jsx)(a.h3,{id:"all",children:"all"}),"\n",(0,l.jsx)(a.pre,{children:(0,l.jsx)(a.code,{className:"language-clojure",children:"(all s)\n"})}),"\n",(0,l.jsxs)(a.p,{children:["Obscure the whole string. Useful for passwords\n",(0,l.jsx)("p",{children:(0,l.jsx)("sub",{children:(0,l.jsx)("a",{href:"https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L17-L20",children:"Source"})})})]}),"\n",(0,l.jsx)(a.h3,{id:"email",children:"email"}),"\n",(0,l.jsx)(a.pre,{children:(0,l.jsx)(a.code,{className:"language-clojure",children:"(email s)\n"})}),"\n",(0,l.jsx)(a.p,{children:"Obscure an email. Examples:"}),"\n",(0,l.jsxs)(a.ul,{children:["\n",(0,l.jsxs)(a.li,{children:[(0,l.jsx)(a.a,{href:"mailto:some.email@gmail.com",children:"some.email@gmail.com"})," -> so******",(0,l.jsx)(a.a,{href:"mailto:il@gmail.com",children:"il@gmail.com"})]}),"\n",(0,l.jsxs)(a.li,{children:[(0,l.jsx)(a.a,{href:"mailto:small@gmail.com",children:"small@gmail.com"})," -> s****@gmail.com"]}),"\n",(0,l.jsxs)(a.li,{children:[(0,l.jsx)(a.a,{href:"mailto:bob@gmail.com",children:"bob@gmail.com"})," -> b**@gmail.com\n",(0,l.jsx)("p",{children:(0,l.jsx)("sub",{children:(0,l.jsx)("a",{href:"https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L22-L28",children:"Source"})})})]}),"\n"]}),"\n",(0,l.jsx)(a.h3,{id:"offset",children:"offset"}),"\n",(0,l.jsx)(a.pre,{children:(0,l.jsx)(a.code,{className:"language-clojure",children:"(offset s opts)\n"})}),"\n",(0,l.jsxs)(a.p,{children:["Given a string, obscure all the string content from ",(0,l.jsx)(a.code,{children:"start"}),"+",(0,l.jsx)(a.code,{children:"start-offset"})," to ",(0,l.jsx)(a.code,{children:"end"}),"-`end-offset>\nopts:"]}),"\n",(0,l.jsxs)(a.ul,{children:["\n",(0,l.jsxs)(a.li,{children:[(0,l.jsx)(a.code,{children:":start"}),": amount of chars at the beginning of the string to not be obscured"]}),"\n",(0,l.jsxs)(a.li,{children:[(0,l.jsx)(a.code,{children:":end"}),": amount of chars at the end of the string to not be obscured\n",(0,l.jsx)("p",{children:(0,l.jsx)("sub",{children:(0,l.jsx)("a",{href:"https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L4-L10",children:"Source"})})})]}),"\n"]}),"\n",(0,l.jsx)(a.h3,{id:"phone",children:"phone"}),"\n",(0,l.jsx)(a.pre,{children:(0,l.jsx)(a.code,{className:"language-clojure",children:"(phone s)\n"})}),"\n",(0,l.jsxs)(a.p,{children:["Obscure a phone number. It removes phone number's specific special characters\nsuch as ",(0,l.jsx)(a.code,{children:"()+"})," and white spaces\n",(0,l.jsx)("p",{children:(0,l.jsx)("sub",{children:(0,l.jsx)("a",{href:"https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L30-L34",children:"Source"})})})]}),"\n",(0,l.jsx)(a.h3,{id:"symmetric-offset",children:"symmetric-offset"}),"\n",(0,l.jsx)(a.pre,{children:(0,l.jsx)(a.code,{className:"language-clojure",children:"(symmetric-offset s n)\n"})}),"\n",(0,l.jsxs)(a.p,{children:["Same as ",(0,l.jsx)(a.a,{href:"#offset",children:(0,l.jsx)(a.code,{children:"offset"})})," but ",(0,l.jsx)(a.code,{children:":start"})," and ",(0,l.jsx)(a.code,{children:":end"})," are equal\n",(0,l.jsx)("p",{children:(0,l.jsx)("sub",{children:(0,l.jsx)("a",{href:"https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L12-L15",children:"Source"})})})]})]})}function h(e={}){const{wrapper:a}={...(0,s.M)(),...e.components};return a?(0,l.jsx)(a,{...e,children:(0,l.jsx)(d,{...e})}):d(e)}},2172:(e,a,n)=>{n.d(a,{I:()=>c,M:()=>r});var l=n(1504);const s={},o=l.createContext(s);function r(e){const a=l.useContext(o);return l.useMemo((function(){return"function"==typeof e?e(a):{...a,...e}}),[a,e])}function c(e){let a;return a=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:r(e.components),l.createElement(o.Provider,{value:a},e.children)}}}]);