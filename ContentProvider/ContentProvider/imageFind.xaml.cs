using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace ContentProvider
{
    /// <summary>
    /// imageFind.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class imageFind : Window
    {
        public imageFind()
        {
            InitializeComponent();
        }

        private string Request_Json()
        {
            string result = null;
            string url = "https://apis.daum.net/search/image?apikey=a509068e1960142d8d73fa4240a49ae1&q=" + textBox.Text + "&output=json";

            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                Stream stream = response.GetResponseStream();
                StreamReader reader = new StreamReader(stream);
                result = reader.ReadToEnd();
                stream.Close();
                response.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

            return result;
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                JObject obj = JObject.Parse(Request_Json());
                JArray array = JArray.Parse(obj["channel"]["item"].ToString());

                foreach (JObject itemObj in array)
                    Data.Record(itemObj["image"].ToString());

                imageView imgv = new imageView();
                imgv.Owner = this;
                imgv.Show();
            }

            catch(Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }
        }
    }
}
