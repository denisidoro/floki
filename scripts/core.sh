function platform::command_exists() {
   type "$1" &>/dev/null
}

function platform::is_osx() {
   [[ $(uname -s) == "Darwin" ]]
}

if platform::is_osx && platform::command_exists ggrep; then
   function sed() { gsed "$@"; }
   function awk() { gawk "$@"; }
   function find() { gfind "$@"; }
   function grep() { ggrep "$@"; }
   function head() { ghead "$@"; }
   function mktemp() { gmktemp "$@"; }
   function date() { gdate "$@"; }
   function shred() { gshred "$@"; }
   function cut() { gcut "$@"; }
   function tr() { gtr "$@"; }
   function od() { god "$@"; }
   function cp() { gcp "$@"; }
   function cat() { gcat "$@"; }
   function sort() { gsort "$@"; }
   function kill() { gkill "$@"; }
   function xargs() { gxargs "$@"; }
   export -f sed awk find head mktemp date shred cut tr od cp cat sort kill xargs
fi
