"use strict";(self.webpackChunkbig_bang=self.webpackChunkbig_bang||[]).push([[6544],{7564:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>d,contentTitle:()=>i,default:()=>h,frontMatter:()=>r,metadata:()=>o,toc:()=>l});var a=n(7624),s=n(2172);const r={sidebar_position:0,custom_edit_url:"https://github.com/yuhrao/big-bang/tree/main/README.md"},i="Database",o={id:"Components/database/index",title:"Database",description:"Iteract with databases",source:"@site/docs/Components/database/index.md",sourceDirName:"Components/database",slug:"/Components/database/",permalink:"/big-bang/Components/database/",draft:!1,unlisted:!1,editUrl:"https://github.com/yuhrao/big-bang/tree/main/README.md",tags:[],version:"current",sidebarPosition:0,frontMatter:{sidebar_position:0,custom_edit_url:"https://github.com/yuhrao/big-bang/tree/main/README.md"},sidebar:"tutorialSidebar",previous:{title:"core",permalink:"/big-bang/Components/config/api/yuhrao/config/core/"},next:{title:"column-readers",permalink:"/big-bang/Components/database/api/yuhrao/database/column-readers/"}},d={},l=[{value:"Libs",id:"libs",level:2},{value:"How to add a new database",id:"how-to-add-a-new-database",level:2},{value:"How to create migrations",id:"how-to-create-migrations",level:2}];function c(e){const t={a:"a",code:"code",h1:"h1",h2:"h2",li:"li",p:"p",pre:"pre",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",ul:"ul",...(0,s.M)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.h1,{id:"database",children:"Database"}),"\n",(0,a.jsx)(t.p,{children:"Iteract with databases"}),"\n",(0,a.jsx)(t.p,{children:(0,a.jsx)(t.strong,{children:"Table of Contents"})}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.a,{href:"#database",children:"Database"}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsx)(t.li,{children:(0,a.jsx)(t.a,{href:"#libs",children:"Libs"})}),"\n",(0,a.jsx)(t.li,{children:(0,a.jsx)(t.a,{href:"#how-to-add-a-new-database",children:"How to add a new database"})}),"\n",(0,a.jsx)(t.li,{children:(0,a.jsx)(t.a,{href:"#how-to-create-migrations",children:"How to create migrations"})}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,a.jsx)(t.h2,{id:"libs",children:"Libs"}),"\n",(0,a.jsxs)(t.table,{children:[(0,a.jsx)(t.thead,{children:(0,a.jsxs)(t.tr,{children:[(0,a.jsx)(t.th,{children:"Lib"}),(0,a.jsx)(t.th,{children:"Description"})]})}),(0,a.jsxs)(t.tbody,{children:[(0,a.jsxs)(t.tr,{children:[(0,a.jsx)(t.td,{children:"Ragtime"}),(0,a.jsx)(t.td,{children:"Database Migrations"})]}),(0,a.jsxs)(t.tr,{children:[(0,a.jsx)(t.td,{children:"Next JDBC"}),(0,a.jsx)(t.td,{children:"Execute SQL statements"})]}),(0,a.jsxs)(t.tr,{children:[(0,a.jsx)(t.td,{children:"Honey SQL"}),(0,a.jsx)(t.td,{children:"Data driven SQL statements"})]})]})]}),"\n",(0,a.jsx)(t.h2,{id:"how-to-add-a-new-database",children:"How to add a new database"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-clojure",children:'(let [db-spec-1 {:datastore {:dbtype   "postgres"\n                             :dbname   "psql"\n                             :host     "localhost"\n                             :port     5432\n                             :user     "app"\n                             :password "app"}}\n      db-spec-2 {:datastore {:dbtype   "postgres"\n                             :dbname   "other-db"\n                             :host     "other.postgres.db"\n                             :port     5432\n                             :user     "app"\n                             :password "app"}}]\n  ...)\n'})}),"\n",(0,a.jsx)(t.h2,{id:"how-to-create-migrations",children:"How to create migrations"}),"\n",(0,a.jsxs)(t.p,{children:["You can follow instructions in ",(0,a.jsx)(t.a,{href:"https://github.com/abogoyavlensky/automigrate",children:"automigrate"})]}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-clojure",children:'{:aliases {:your-alias {:ns-default automigrate.core\n                        :exec-args  {:models-file    "resources/db/models.edn"\n                                     :migrations-dir "resources/db/migrations"\n                                     :jdbc-url       "jdbc:postgresql://localhost:5432/mydb?user=myuser&password=secret"}}}}\n'})}),"\n",(0,a.jsx)(t.p,{children:"Run migrations following automigrate instructions"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-shell",children:"# 1- Create generate migration files\nclojure -X:migrations make\n## With Custom Name\nclojure -X:migrations make :name <name>\n\n# 2- Execute migrations\n\n# Dev\nclojure -X:migrations migrate\n# Prod\nclojure -X:migrations migrate :jdbc-url $DATABASE_URL\n"})})]})}function h(e={}){const{wrapper:t}={...(0,s.M)(),...e.components};return t?(0,a.jsx)(t,{...e,children:(0,a.jsx)(c,{...e})}):c(e)}},2172:(e,t,n)=>{n.d(t,{I:()=>o,M:()=>i});var a=n(1504);const s={},r=a.createContext(s);function i(e){const t=a.useContext(r);return a.useMemo((function(){return"function"==typeof e?e(t):{...t,...e}}),[t,e])}function o(e){let t;return t=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:i(e.components),a.createElement(r.Provider,{value:t},e.children)}}}]);