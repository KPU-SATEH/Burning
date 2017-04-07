using SHDocVw;
using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Xml;

namespace ContentProvider
{
    /// <summary>
    /// Interaction logic for FileChoiceWindow.xaml
    /// </summary>

    public partial class FileChoiceWindow : Window
    {
        public FileChoiceWindow()
        {
            InitializeComponent();

            ReadFromServer();
        }

        /*
        public SHDocVw.WebBrowser FindIE(string url)
        {
            Uri uri = new Uri(url);
            var shellWindows = new SHDocVw.ShellWindows();
            foreach (SHDocVw.WebBrowser wb in shellWindows)
            {
                if (!string.IsNullOrEmpty(wb.LocationURL))
                {
                    Uri wbUri = new Uri(wb.LocationURL);
                    System.Diagnostics.Debug.WriteLine(wbUri);
                    if (wbUri.Equals(uri))
                        return wb;
                }
            }
            return null;
        }
        */

        private void listview_Click(object sender, RoutedEventArgs e)
        {
            var item = (sender as ListView).SelectedItem;

            if (item != null) 
            {
                try
                {
                    string str = item.ToString();
                    byte[] utf8bytes = Encoding.UTF8.GetBytes(str);

                    var ie = new InternetExplorer();
                    var webBrowser = (IWebBrowserApp)ie;
                    webBrowser.Visible = true;
                    webBrowser.Navigate("http://computer.kevincrack.com/download.jsp?name=" + System.Web.HttpUtility.UrlEncode(utf8bytes));

                    //SHDocVw.WebBrowser wb = FindIE("http://computer.kevincrack.com/download.jsp?name=" + System.Web.HttpUtility.UrlEncode(utf8bytes));
                    //wb.Quit();
                }
                catch (Exception)
                {
                }

                this.Close();
            }
        }

        public void ReadFromServer()
        {
            string url = @"http://computer.kevincrack.com/epub_download.jsp";

            try
            {
                XmlTextReader reader = new XmlTextReader(url);

                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Text)
                    {
                        lv.Items.Add(reader.Value);
                        reader.MoveToAttribute("storename");
                    }
                }
            }
            catch (Exception)
            {
            }
        }
    }
}
