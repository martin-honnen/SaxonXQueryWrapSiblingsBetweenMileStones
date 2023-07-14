declare namespace output = "http://www.w3.org/2010/xslt-xquery-serialization";

declare option output:method 'xml';
declare option output:indent 'yes';

declare function local:transform($node as node(), $siblings-to-be-wrapped as array(element()*)*) {
  typeswitch ($node)
    case document-node()
      return document { $node!node()!local:transform(., $siblings-to-be-wrapped) }
    case element()
      return
        let $match := $siblings-to-be-wrapped[some $el in ?* satisfies $el is $node]
        return if ($node is $match?1)
               then <added>{$match}</added>
               else if (not(exists($match)))
               then element { node-name($node) } { $node!@*, $node!node()!local:transform(., $siblings-to-be-wrapped) }
               else ()
    default
      return $node
};

let $start-elements := //start,
    $stop-elements := //stop,
    $siblings-to-be-wrapped := for-each-pair(
      $start-elements,
      $stop-elements,
      function($s, $e) {
        let $elements-to-be-wrapped := outermost(root($s)//*[. >> $s and . << $e][not(some $d in .//* satisfies ($d is $s or $d is $e))])
        for tumbling window $siblings in $elements-to-be-wrapped
        start $s when true()
        end next $n when not($s/.. is $n/..)
        return array { $siblings }
      }
    )
return
    local:transform(/, $siblings-to-be-wrapped)