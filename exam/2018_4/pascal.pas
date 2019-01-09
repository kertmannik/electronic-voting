procedure TForm1.Button1Click(Sender: TObject);
var
  sgid: Integer;
  lgid: Integer;
  eid: String;
  conn : TODBCConnection; // uses ODBCConn
  query: TSQLQuery;       // uses sqldb
  transaction: TSQLTransaction;   // uses sqldb
begin
  conn := TODBCCOnnection.Create(nil);
  query := TSQLQuery.Create(nil);
  transaction := TSQLTransaction.Create(nil);
  try
    try
      … //DB initialisation removed to save space
      conn.Transaction := transaction;
      query.DataBase := conn;
      query.SQL.Text := 'select * from entries where groupnr > :sgid and groupnr < :lgid and entryid = ' + eid; //: is used as parameter identifier
      query.Params.ParamByName('lgid').AsInteger := lgid;
      query.Open; //Automatically prepares statement
      while not query.EOF do
      begin
        … //DB results processing removed to save space
        query.Next;
      end;
    finally
      query.Free;
      conn.Free; // Automatically closes the connection
      transaction.Free;
    end;
  except
    on E: Exception do
      ShowMessage(E.message);
  end;
end;

viga: puudu sgid query.Params.ParamByName('sgid').AsInteger := sgid;
haavatavus: eid peaks olema nii: query.Params.ParamByName('eid') := eid; (muuda ka sql-i)
hea tava: select päringu asemel protseduur, kasutame vaateid, mitte tabeleid otse